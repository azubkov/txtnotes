@Required

@Autowired(required=false)

@Autowired = @Inject

@Bean
@Primary
<bean class="example.SimpleMovieCatalog" primary=true>
@Qualifier 

<qualifier value="main"/>
@Autowired
@Qualifier("main")
public void prepare(@Qualifier("main")MovieCatalog movieCatalog
For a fallback match, the bean name is considered a default qualifier value. 
<qualifier type="example.Genre" value="Comedy"/>



@Resource  - by bean id or name
@Resource(name="myMovieFinder")


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Offline {
}


@Autowired
@Offline
private MovieCatalog offlineCatalog;

<qualifier type="Offline"/>


@Scope, 
@Lazy,  In this context, it leads to the injection of a lazy-resolution proxy.

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MovieQualifier {
String genre();
Format format();
}
In this case Format is an enum:
public enum Format {
VHS, DVD, BLURAY
}


<qualifier type="MovieQualifier">
<attribute key="format" value="VHS"/>
<attribute key="genre" value="Action"/>
</qualifier>
@Autowired
@MovieQualifier(format=Format.BLURAY, genre="Comedy")
private MovieCatalog comedyBluRayCatalog;

@PostConstruct and @PreDestroy
@Component
@Repository
@Component, @Service, and @Controller.
The difference is that @Component classes are not enhanced
with CGLIB to intercept the invocation of methods and fields

@Scope("prototype")
@Scope
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Scope(BeanDefinition.SCOPE_SINGLETON)



@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Scope("session")
public @interface SessionScope {
  ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
}


@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {
...
}
<context:component-scan base-package="org.example"/>

@Configuration
@ComponentScan(basePackages = "org.example",
includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
excludeFilters = @Filter(Repository.class))
public class AppConfig {

<context:component-scan base-package="org.example">
<context:include-filter type="regex"
expression=".*Stub.*Repository"/>
<context:exclude-filter type="annotation"
expression="org.springframework.stereotype.Repository"/>
</context:component-scan>
use-default-filters="false"

@Bean
protected TestBean protectedInstance(
  @Qualifier("public") TestBean spouse,
  @Value("#{privateInstance.age}") String country) {
    TestBean tb = new TestBean("protectedInstance", 1);
    tb.setSpouse(spouse);
    tb.setCountry(country);
    return tb;
}
The example autowires the String method parameter country to the value of the Age property on
another bean named privateInstance


@Service("myMovieLister")
BeanNameGenerator interface

@Configuration
@ComponentScan(basePackages = "org.example", nameGenerator = MyNameGenerator.class)
public class AppConfig {

<beans>
  <context:component-scan base-package="org.example"
    name-generator="org.example.MyNameGenerator" />
</beans>

InitializingBean, DisposableBean, or Lifecycle, interface 
 BeanFactoryAware, BeanNameAware,
MessageSourceAware, ApplicationContextAware  interface
ScopeMetadataResolver interface
@Configuration
@ComponentScan(basePackages = "org.example", scopeResolver = MyScopeResolver.class)
public class AppConfig {
...
}
<beans>
<context:component-scan base-package="org.example"
scope-resolver="org.example.MyScopeResolver"</beans>

@Configuration
@ComponentScan(basePackages = "org.example", scopedProxy = ScopedProxyMode.INTERFACES)
public class AppConfig {
...
}
<beans>
<context:component-scan base-package="org.example"
scoped-proxy="interfaces" />
</beans>

@Named("main")=@Component
@Singleton

The @Bean annotation is used to indicate that a method instantiates, configures and initializes a
new object to be managed by the Spring IoC container.


Annotating a class with @Configuration indicates that its primary purpose is as a source of bean
definitions. 

When @Bean methods are declared within classes that are not annotated with @Configuration
they are referred to as being processed in a 'lite' mode. 


When @Configuration classes are provided as input, the @Configuration class itself is registered
as a bean definition, and all declared @Bean methods within the class are also registered as bean
definitions.


public static void main(String[] args) {
  ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
  MyService myService = ctx.getBean(MyService.class);
  myService.doStuff();
}

AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.register(AppConfig.class, OtherConfig.class);
ctx.register(AdditionalConfig.class);
ctx.refresh();
MyService myService = ctx.getBean(MyService.class);
myService.doStuff();


@Configuration
@ComponentScan(basePackages = "com.acme")
public class AppConfig {
...
}
<beans>
<context:component-scan base-package="com.acme"/>
</beans>


public static void main(String[] args) {
  AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
  ctx.scan("com.acme");
  ctx.refresh();
  MyService myService = ctx.getBean(MyService.class);
}

 <aop:scoped-proxy/>
 
Bean aliasing

@Bean(name = { "dataSource", "subsystemA-dataSource", "subsystemB-dataSource" })
public DataSource dataSource() {
// instantiate, configure and return DataSource bean...
}

@Bean
@Description("Provides a basic example of a bean")

 lookup method injection  is useful
in cases where a singleton-scoped bean has a dependency on a prototype-scoped bean. 

@Bean
@Scope("prototype")
public AsyncCommand asyncCommand() {
  AsyncCommand command = new AsyncCommand();
    // inject dependencies here as required
    return command;
  }
  @Bean
  public CommandManager commandManager() {
    // return new anonymous implementation of CommandManager with command() overridden
    // to return a new prototype Command object
    return new CommandManager() {
    protected Command createCommand() {
    return asyncCommand();
    }
  }
}

//clientDao is called once:

@Configuration
public class AppConfig {
@Bean
public ClientService clientService1() {
ClientServiceImpl clientService = new ClientServiceImpl();
clientService.setClientDao(clientDao());
return clientService;
}
@Bean
public ClientService clientService2() {
ClientServiceImpl clientService = new ClientServiceImpl();
clientService.setClientDao(clientDao());
return clientService;
}
@Bean
public ClientDao clientDao() {
return new ClientDaoImpl();
}
}


@Configuration
@Import(ConfigA.class)
public class ConfigB {


The example above works, but is simplistic. In most practical scenarios, beans will have dependencies
on one another across configuration classes. When using XML, this is not an issue, per se, because
there is no compiler involved, and one can simply declare ref="someBean" and trust that Spring will
work it out during container initialization. Of course, when using @Configuration classes, the Java
compiler places constraints on the configuration model, in that references to other beans must be valid
Java syntax.


In the scenario above, using @Autowired works well and provides the desired modularity, but
determining exactly where the autowired bean definitions are declared is still somewhat ambiguous. For
example, as a developer looking at ServiceConfig, how do you know exactly where the @Autowired
AccountRepository bean is declared? It’s not explicit in the code, and this may be just fine.
Remember that the Spring Tool Suite provides tooling that can render graphs showing how everything
is wired up - that may be all you need. Also, your Java IDE can easily find all declarations and uses of
the AccountRepository type, and will quickly show you the location of @Bean methods that return
that type.



@Configuration
public class ServiceConfig {
@Autowired
private RepositoryConfig repositoryConfig;
@Bean
public TransferService transferService() {
// navigate 'through' the config class to the @Bean method!
return new TransferServiceImpl(repositoryConfig.accountRepository());
}
}

@Profile 
@Conditional. 



<beans>
<!-- enable processing of annotations such as @Autowired and @Configuration -->
<context:annotation-config/>
<context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>
<bean class="com.acme.AppConfig"/>
<bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
<property name="url" value="${jdbc.url}"/>
<property name="username" value="${jdbc.username}"/>
<property name="password" value="${jdbc.password}"/>
</bean>
</beans>



@Configuration
@ImportResource("classpath:/com/acme/properties-config.xml")
public class AppConfig {
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Bean
    public DataSource dataSource() {
    return new DriverManagerDataSource(url, username, password);
    }
}

@Profile("dev")
@Profile({"p1", "!p2"})
<beans profile="production">
  <jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
</beans>
ctx.getEnvironment().setActiveProfiles("dev");
-Dspring.profiles.active
@ActiveProfiles
-Dspring.profiles.active="profile1,profile2"
The default profile represents the profile that is enabled by default. Consider the following:
@Profile("default")
 If any profile is enabled, the default profile will not apply.
The name of the default profile can be changed using setDefaultProfiles() on the Environment
or declaratively using the spring.profiles.default property.



Environment env = ctx.getEnvironment();
boolean containsFoo = env.containsProperty("foo");

PropertySources interface
ConfigurableApplicationContext ctx = new GenericApplicationContext();
MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
sources.addFirst(new MyPropertySource());
@PropertySource
@PropertySource("classpath:/com/${my.placeholder:default/path}/app.properties")
 If not, then "default/path" will be used as a default. 
 
@EnableLoadTimeWeaving
<beans>
<context:load-time-weaver/>
</beans>
LoadTimeWeaverAware, interface
ApplicationContext interface

MessageSource interface
ResourceLoader  interface
ApplicationListener  interface
ApplicationEventPublisher  interface
HierarchicalBeanFactory  interface
When an ApplicationContext is loaded, it automatically searches for a MessageSource bean
defined in the context. The bean must have the name messageSource. 
DelegatingMessageSource class 


<beans>
  <bean id="messageSource"
  class="org.springframework.context.support.ResourceBundleMessageSource">
  <property name="basenames">
    <list>
      <value>format</value>
      <value>exceptions</value>
      <value>windows</value>
    </list>
  </property>
  </bean>
</beans>
// format.properties
// exceptions.properties
// etc
# in exceptions.properties
argument.required=The '{0}' argument is required.
String message = this.messages.getMessage("argument.required",
new Object [] {"userDao"}, "Required", null);
if you want to resolve messages
against the British (en-GB) locale, you would create files called format_en_GB.properties,
exceptions_en_GB.properties, and windows_en_GB.properties respectively.
MessageSourceAware interface

ApplicationEvent
ApplicationListener
PayloadApplicationEvent
ApplicationEventPublisher
ApplicationEventPublisherAware
You may register as many event listeners as you wish, but note that
by default event listeners receive events synchronously. 
ApplicationEventMulticaster
publisher.publishEvent(event);
for more sophisticated enterprise integration needs,
the separately-maintained Spring Integration project provides complete support for building
lightweight, pattern-oriented, event-driven architectures that build upon the well-known Spring
programming model.

@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
@EventListener(condition = "#event.test == 'foo'")

If you need to publish an event as the result of processing another, just change the method signature
to return the event that should be published, something like:
@EventListener
public ListUpdateEvent handleBlackListEvent(BlackListEvent event) {
// notify appropriate parties via notificationAddress and
// then publish a ListUpdateEvent...
}

If you need to publish several events, just return a Collection of events instead.


Finally if you need the listener to be invoked before another one, just add the @Order annotation to
the method declaration:

@Order(42)


You may also use generics to further define the structure of your event. Consider an
EntityCreatedEvent<T> where T is the type of the actual entity that got created. You can create
the following listener definition to only receive EntityCreatedEvent for a Person:
@EventListener
public void onPersonCreated(EntityCreatedEvent<Person> event) {
...
}

ResolvableTypeProvider interface



ResourceLoader, interface
ResourceLoaderAware,
JavaBean PropertyEditor that is automatically registered by the context, to convert those text strings to actual
Resource objects when the bean is deployed.

Creating context:
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>/WEB-INF/daoContext.xml /WEB-INF/applicationContext.xml</param-value>
</context-param>
<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

The listener inspects the contextConfigLocation parameter. If the parameter does not exist, the
listener uses /WEB-INF/applicationContext.xml as a default. 
 Ant-style path patterns
are supported as well. Examples are /WEB-INF/*Context.xml 

*/
JCA WorkManager through Spring’s TaskExecutor
abstraction.

BeanFactory
BeanFactoryAware,
InitializingBean,
DisposableBean,  purposes of backward
compatibility

BeanFactoryPostProcessor interface
ContextSingletonBeanFactoryLocator 


ResourceLoader interface
Resource interface


/data/config.xml
 Depends on the underlying
ApplicationContext.
ResourceLoaderAware


So if
myBean has a template property of type Resource, it can be configured with a simple string for that
resource, as follows:


<bean id="myBean" class="...">
  <property name="template" value="some/resource/path/myTemplate.txt"/>
</bean>


 if you create a ClassPathXmlApplicationContext as follows:
ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/appContext.xml");
The bean definitions will be loaded from the classpath, as a ClassPathResource will be used. But if
you create a FileSystemXmlApplicationContext as follows:
ApplicationContext ctx =
new FileSystemXmlApplicationContext("conf/appContext.xml");


Please note that classpath*: when combined with Ant-style patterns will only work reliably with at
least one root directory before the pattern starts, unless the actual target files reside in the file system.
This means that a pattern like " classpath*:*.xml`" will not retrieve files from the
root of jar files but rather only from the root of expanded directories. This
originates from a limitation in the JDK’s `ClassLoader.getResources() 

Ant-style patterns with " `classpath:`" resources are not guaranteed to find matching resources if the
root package to search is available in multiple class path locations. This is because a resource such as

For backwards compatibility (historical) reasons however, 
this changeswhen the FileSystemApplicationContext is the ResourceLoader. 
The FileSystemApplicationContext simply forces all attached FileSystemResource instances to
treat all location paths as relative, whether they start with a leading slash or not. 


Validator interface
DataBinder interface
BeanWrapper interface
BeanWrapperImpl). class
Errors
ValidationUtils
ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
errors.popNestedPath();
 <spring:bind/> 
 MessageCodesResolver
 DefaultMessageCodesResolver
 in case you reject a field using rejectValue("age", "too.darn.old"), apart from the
too.darn.old code, Spring will also register too.darn.old.age and too.darn.old.age.int
(so the first will include the field name and the second will include the type of the field); 
 
PropertyChangeListeners
VetoableChangeListeners,
PropertyEditoClassEditorrs
CommandController.
ByteArrayPropertyEditor
CustomBooleanEditor
CustomCollectionEditor
CustomDateEditor
CustomNumberEditor
FileEditor
InputStreamEditor
LocaleEditor
PatternEditor
PropertiesEditor
StringTrimmerEditor
URLEditor
java.beans.PropertyEditorManager

Note also that the standard JavaBeans infrastructure will automatically discover PropertyEditor
classes (without you having to register them explicitly) if they are in the same package as the class
they handle, and have the same name as that class, with 'Editor'
BeanInfo


PropertyEditor interface
ConfigurableBeanFactory interface
CustomEditorConfigurer has a nested property
setup, so it is strongly recommended that it is used with the ApplicationContext, where it may be
deployed in similar fashion to any other bean, and automatically detected and applied.
Anonymus:
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
  <property name="customEditors">
    <map>
    <entry key="example.ExoticType" value="example.ExoticTypeEditor"/>
    </map>
  </property>
</bean>


PropertyEditorRegistry,
PropertyEditorRegistrar
DataBinder
ResourceEditorRegistrar
Controller

public interface Converter<S, T> {
  T convert(S source);
}

DefaultConversionService
 Take care to ensure that your Converter
implementation is thread-safe.

ConverterFactory
public interface ConverterFactory<S, R> {
  <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
GenericConverter
TypeDescriptor
ConvertiblePair
ConditionalGenericConverter
ConversionService
ConverterRegistry
GenericConversionService
ConversionServiceFactory
FormattingConversionServiceFactoryBean
DefaultConversionService

Formatter
Printer<T>
arser<T> 
AnnotationFormatterFactory<A

@NumberFormat(style=Style.CURRENCY)
private BigDecimal decimal;

@DateTimeFormat(iso=ISO.DATE)
private Date date;


FormattingConversionService
FormatterRegistry
FormattingConversionServiceFactoryBean.
ConversionService,
FormatterRegistrar
DateFormat.SHORT 
JodaTimeFormatterRegistrar
DateFormatterRegistrar 

<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
  <property name="registerDefaultFormatters" value="false" />
  <property name="formatters">
    <set>
      <bean class="org.springframework.format.number.NumberFormatAnnotationFormatterFactory" /
      >
    </set>
  </property>
  <property name="formatterRegistrars">
    <set>
      <bean class="org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar">
      <property name="dateFormatter">
      <bean class="org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean">
      <property name="pattern" value="yyyyMMdd"/>
      </bean>
      </property>
      </bean>
    </set>
  </property>
</bean>

conversion-service
mvc:annotation-driven
WebMvcConfigurationSupport



@NotNull
@Size(max=64)
private String name;

@Min(0)
private int age;

ValidatorFactory
Validator
LocalValidatorFactoryBean
@Constraint
javax.validation.ConstraintValidator
ValidationConstraint
ConstraintValidatorFactory
SpringConstraintValidatorFactory
ValidationUtils

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
}


public class MyConstraintValidator implements ConstraintValidator {
@Autowired;
private Foo aDependency;
...
}
MethodValidationPostProcessor
<bean class="org.springframework.validation.beanvalidation.MethodValidationPostProcessor"/>
 @Validated
 
binder.validate()
BindingResult.


Foo target = new Foo();
DataBinder binder = new DataBinder(target);
binder.setValidator(new FooValidator());
// bind to the target object
binder.bind(propertyValues);
// validate the target object
binder.validate();
// get BindingResult that includes any validation errors
BindingResult results = binder.getBindingResult();




ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'");
String message = (String) exp.getValue();

ExpressionParser
ParseException
EvaluationException

ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("'Hello World'.concat('!')");
String message = (String) exp.getValue();

//calling a JavaBean property
ExpressionParser parser = new SpelExpressionParser();
// invokes 'getBytes()'
Expression exp = parser.parseExpression("'Hello World'.bytes");
byte[] bytes = (byte[]) exp.getValue();


ExpressionParser parser = new SpelExpressionParser();
// invokes 'getBytes().length'
Expression exp = parser.parseExpression("'Hello World'.bytes.length");
int length = (Integer) exp.getValue();


ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
String message = exp.getValue(String.class);



// Create and set a calendar
GregorianCalendar c = new GregorianCalendar();
c.set(1856, 7, 9);
// The constructor arguments are name, birthday, and nationality.
Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");
ExpressionParser parser = new SpelExpressionParser();
Expression exp = parser.parseExpression("name");
EvaluationContext context = new StandardEvaluationContext(tesla);
String name = (String) exp.getValue(context);




Expression exp = parser.parseExpression("name == 'Nikola Tesla'");
boolean result = exp.getValue(context, Boolean.class);
EvaluationContext
StandardEvaluationContext,
ConstructorResolver, 
MethodResolver,  
PropertyAccessor
ConversionService

parser.parseExpression("booleanList[0]").setValue(simpleContext, "false");
SpelParserConfiguration

// - auto null reference initialization
// - auto collection growing
SpelParserConfiguration config = new SpelParserConfiguration(true,true);
ExpressionParser parser = new SpelExpressionParser(config);

SpelCompilerMode
The system property spring.expression.compiler.mode can be set to one of
the SpelCompilerMode enum values (off, immediate, or mixed).
 #{ <expression string> }
<property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>
<property name="defaultLocale" value="#{ systemProperties['user.region'] }"/>
<property name="initialShapeSeed" value="{ numberGuess.randomNumber }"/>
@Value("#{ systemProperties['user.region'] }")
public void configure(MovieFinder movieFinder, @Value("#{ systemProperties['user.region'] }") String defaultLocale) 

Strings are delimited by single quotes. To put a single quote itself in a string use two single
quote characters. 
Numbers support the use of the negative sign, exponential notation, and decimal points. B

int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(context);
String invention = parser.parseExpression("inventions[3]").getValue(
teslaContext, String.class);
Inventor pupin = parser.parseExpression("Officers['president']").getValue(societyContext, Inventor.class);

List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(context);
List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);

{} by itself means an empty list. For performance reasons, if the list is itself entirely composed of fixed
literals then a constant list is created to represent the expression, rather than building a new list on
each evaluation.

Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);

{:} by itself means an empty map. For performance reasons, if the map is itself composed of fixed
literals or other nested constant structures (lists or maps) then a constant map is created to represent
the expression, rather than building a new map on each evaluation. Quoting of the map keys is optional,
the examples above are not using quoted keys.


String c = parser.parseExpression("'abc'.substring(2, 3)").getValue(String.class);
boolean falseValue = parser.parseExpression(
"'xyz' instanceof T(int)").getValue(Boolean.class);


boolean falseValue = parser.parseExpression(
"'5.0067' matches '\^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
 lt (<), gt (>), le (#),(>=), eq (==), ne (!=), div (/), mod (%), not (!)

 
 
The logical operators that are supported are and, or, and not.
boolean falseValue = parser.parseExpression("true and false").getValue(Boolean.class);


String expression = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
boolean trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);

parser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");
String aleks = parser.parseExpression(
"Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);

StandardEvaluationContext
TypeLocator
StandardTypeLocator

Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
boolean trueValue = parser.parseExpression(
"T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR")
.getValue(Boolean.class);


Inventor einstein = p.parseExpression(
"new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German')")
.getValue(Inventor.class);
//create new inventor instance within add method of List
p.parseExpression(
"Members.add(new org.spring.samples.spel.inventor.Inventor(
'Albert Einstein', 'German'))").getValue(societyContext);

context.setVariable("newName", "Mike Tesla");
parser.parseExpression("Name = #newName").getValue(context);


context.setVariable("primes",primes);
// all prime numbers > 10 from the list (using selection ?{...})
// evaluates to [11, 13, 17]
List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
"#primes.?[#this>10]").getValue(context);


ontext.registerFunction("reverseString",
StringUtils.class.getDeclaredMethod("reverseString", new Class[] { String.class }));
String helloWorldReversed = parser.parseExpression(
"#reverseString('hello')").getValue(context, String.class);



ExpressionParser parser = new SpelExpressionParser();
StandardEvaluationContext context = new StandardEvaluationContext();
context.setBeanResolver(new MyBeanResolver());
// This will end up calling resolve(context,"foo") on MyBeanResolver during evaluation
Object bean = parser.parseExpression("@foo").getValue(context);


String falseString = parser.parseExpression("false ? 'trueExp' : 'falseExp'").getValue(String.class);


Elvis operator,
String name = parser.parseExpression("null?:'Unknown'").getValue(String.class);

Safe Navigation operator
String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);

@Value("#{systemProperties['pop3.port'] ?: 25}")

Collection Selection
 ?[selectionExpression].
List<Inventor> list = (List<Inventor>) parser.parseExpression(
"Members.?[Nationality == 'Serbian']").getValue(societyContext);
Map newMap = parser.parseExpression("map.?[value<27]").getValue();


To obtain the first entry matching the selection the syntax is ^[...] whilst to obtain the last matching
selection the syntax is $[...].


Collection Projection  ![projectionExpression]

String randomPhrase = parser.parseExpression(
"random number is #{T(java.lang.Math).random()}",
new TemplateParserContext()).getValue(String.class);




 Aspect: a modularization of a concern that cuts across multiple classes. Transaction management is
a good example of a crosscutting concern in enterprise Java applications. In Spring AOP, aspects
are implemented using regular classes (the schema-based approach) or regular classes annotated
with the @Aspect annotation (the @AspectJ style).
• Join point: a point during the execution of a program, such as the execution of a method or the handling
of an exception. In Spring AOP, a join point always represents a method execution.
 Advice: action taken by an aspect at a particular join point. Different types of advice include "around,"
"before" and "after" advice. (Advice types are discussed below.) Many AOP frameworks, including
Spring, model an advice as an interceptor, maintaining a chain of interceptors around the join point.
• Pointcut: a predicate that matches join points. Advice is associated with a pointcut expression and
runs at any join point matched by the pointcut (for example, the execution of a method with a certain
name). The concept of join points as matched by pointcut expressions is central to AOP, and Spring
uses the AspectJ pointcut expression language by default.
• Introduction: declaring additional methods or fields on behalf of a type. Spring AOP allows you to
introduce new interfaces (and a corresponding implementation) to any advised object. For example,
you could use an introduction to make a bean implement an IsModified interface, to simplify
caching. (An introduction is known as an inter-type declaration in the AspectJ community.)
• Target object: object being advised by one or more aspects. Also referred to as the advised object.
Since Spring AOP is implemented using runtime proxies, this object will always be a proxied object.
• AOP proxy: an object created by the AOP framework in order to implement the aspect contracts
(advise method executions and so on). In the Spring Framework, an AOP proxy will be a JDK dynamic
proxy or a CGLIB proxy.
• Weaving: linking aspects with other application types or objects to create an advised object. This can
be done at compile time (using the AspectJ compiler, for example), load time, or at runtime. Spring
AOP, like other pure Java AOP frameworks, performs weaving at runtime.



Types of advice:
• Before advice: Advice that executes before a join point, but which does not have the ability to prevent
execution flow proceeding to the join point (unless it throws an exception).
• After returning advice: Advice to be executed after a join point completes normally: for example, if a
method returns without throwing an exception.
• After throwing advice: Advice to be executed if a method exits by throwing an exception.
• After (finally) advice: Advice to be executed regardless of the means by which a join point exits (normal
or exceptional return).
• Around advice: Advice that surrounds a join point such as a method invocation. This is the most
powerful kind of advice. Around advice can perform custom behavior before and after the method
invocation. It is also responsible for choosing whether to proceed to the join point or to shortcut the
advised method execution by returning its own return value or throwing an exception.



@AspectJ 
aspectjweaver.jar library is on the classpath of your application
(version 1.6.8 or later).
@Configuration
@EnableAspectJAutoProxy
<aop:aspectj-autoproxy/>


@Aspect
public class NotVeryUsefulAspect {
}


Aspects (classes annotated with @Aspect) may have methods and fields just like any other class. They
may also contain pointcut, advice, and introduction (inter-type) declarations.

@Aspect
@Pointcut

the method serving as the pointcut signature must have a void return type

@Pointcut("execution(* transfer(..))")// the pointcut expression
private void anyOldTransfer() {}// the pointcut signature



• execution - for matching method execution join points, this is the primary pointcut designator you will
use when working with Spring AOP
• within - limits matching to join points within certain types (simply the execution of a method declared
within a matching type when using Spring AOP)
• this - limits matching to join points (the execution of methods when using Spring AOP) where the bean
reference (Spring AOP proxy) is an instance of the given type
• target - limits matching to join points (the execution of methods when using Spring AOP) where the
target object (application object being proxied) is an instance of the given type
• args - limits matching to join points (the execution of methods when using Spring AOP) where the
arguments are instances of the given types
•@target - limits matching to join points (the execution of methods when using Spring AOP) where the
class of the executing object has an annotation of the given type
• @args - limits matching to join points (the execution of methods when using Spring AOP) where the
runtime type of the actual arguments passed have annotations of the given type(s)
• @within - limits matching to join points within types that have the given annotation (the execution of
methods declared in types with the given annotation when using Spring AOP)
•@annotation - limits matching to join points where the subject of the join point (method being executed
in Spring AOP) has the given annotation
bean(idOrNameOfBean)


Due to the proxy-based nature of Spring’s AOP framework, protected methods are by definition
not intercepted, neither for JDK proxies (where this isn’t applicable) nor for CGLIB proxies (where
this is technically possible but not recommendable for AOP purposes). As a consequence, any
given pointcut will be matched against public methods only!



@Pointcut("execution(public * *(..))")
private void anyPublicOperation() {}


@Point@Pointcut("within(com.xyz.someapp.trading..*)")
private void inTrading() {}
cut("within(com.xyz.someapp.trading..*)")
private void inTrading() {}



@Pointcut("anyPublicOperation() && inTrading()")
private void tradingOperation() {}



/**
* A join point is in the web layer if the method is defined
* in a type in the com.xyz.someapp.web package or any sub-package
* under that.
*/
@Pointcut("within(com.xyz.someapp.web..*)")
public void inWebLayer() {}


/**
* A join point is in the service layer if the method is defined
* in a type in the com.xyz.someapp.service package or any sub-package
* under that.
*/
@Pointcut("within(com.xyz.someapp.service..*)")
public void inServiceLayer() {}


/**
* A business service is the execution of any method defined on a service
* interface. This definition assumes that interfaces are placed in the
* "service" package, and that implementation types are in sub-packages.
*
* If you group service interfaces by functional area (for example,
* in packages com.xyz.someapp.abc.service and com.xyz.someapp.def.service) then
* the pointcut expression "execution(* com.xyz.someapp..service.*.*(..))"
* could be used instead.
*
* Alternatively, you can write the expression using the 'bean'
* PCD, like so "bean(*Service)". (This assumes that you have
* named your Spring service beans in a consistent fashion.)
*/
@Pointcut("execution(* com.xyz.someapp..service.*.*(..))")
public void businessService() {}


<aop:config>
  <aop:advisor
    pointcut="com.xyz.someapp.SystemArchitecture.businessService()"
    advice-ref="tx-advice"/>
  </aop:config>
  <tx:advice id="tx-advice">
    <tx:attributes>
      <tx:method name="*" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>

<aop:config> 
<aop:advisor> 




execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern)throws-pattern?)
execution(public * *(..))
execution(* set*(..))
execution(* com.xyz.service.AccountService.*(..))
execution(* com.xyz.service.*.*(..))
execution(* com.xyz.service..*.*(..))
within(com.xyz.service.*)
within(com.xyz.service..*)
this(com.xyz.service.AccountService)
target(com.xyz.service.AccountService)
args(java.io.Serializable)

Note that the pointcut given in this example is different to execution(*
*(java.io.Serializable)): the args version matches if the argument passed at runtime is
Serializable, the execution version matches if the method signature declares a single parameter of type
Serializable.
@target(org.springframework.transaction.annotation.Transactional)
@within(org.springframework.transaction.annotation.Transactional)
@annotation(org.springframework.transaction.annotation.Transactional)
@args(com.xyz.security.Classified)
bean(tradeService)
bean(*Service)






• Kinded designators are those which select a particular kind of join point. For example: execution, get,
set, call, handler
• Scoping designators are those which select a group of join points of interest (of probably many kinds).
For example: within, withincode
• Contextual designators are those that match (and optionally bind) based on context. For example:
this, target, @annotation






@Aspect
public class BeforeExample {
  @Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
  public void doAccessCheck() {
  // ...
  }
}


@AfterReturning 
@Aspect
@Before




@Aspect
public class AfterReturningExample {
  @AfterReturning("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
  public void doAccessCheck() {
  // ...
  }
}

@Aspect
public class AfterReturningExample {
  @AfterReturning(
  pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",
  returning="retVal")
  public void doAccessCheck(Object retVal) {
  // ...
  }
}


@Aspect
public class AfterThrowingExample {
  @AfterThrowing("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
  public void doRecoveryActions() {
  // ...
  }
}


@Aspect
public class AfterThrowingExample {
@AfterThrowing(
pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",
throwing="ex")
public void doRecoveryActions(DataAccessException ex) {
// ...
}
}


@Aspect
public class AfterFinallyExample {
@After("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
public void doReleaseLock() {
// ...
}
}


@Around 
ProceedingJoinPoint.


@Aspect
public class AroundExample {
  @Around("com.xyz.myapp.SystemArchitecture.businessService()")
  public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
  // start stopwatch
  Object retVal = pjp.proceed();
  // stop stopwatch
  return retVal;
  }
}

JoinPoint.

@Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account,..)")
public void validateAccount(Account account) {
// ...
}
 

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {
AuditCode value();
}

@Before("com.xyz.lib.Pointcuts.anyPublicMethod() && @annotation(auditable)")
public void audit(Auditable auditable) {
AuditCode code = auditable.value();
// ...
}

Before("execution(* ..Sample+.sampleGenericMethod(*)) && args(param)")
public void beforeSampleMethod(MyType param) {
// Advice implementation
}

JoinPoint.StaticPart


@Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)",
argNames="bean,auditable")
public void audit(Object bean, Auditable auditable) {
AuditCode code = auditable.value();
// ... use code and bean
}

@Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)",
argNames="bean,auditable")
public void audit(JoinPoint jp, Object bean, Auditable auditable) {
AuditCode code = auditable.value();
// ... use code, bean, and jp
}

the classes
have been compiled with debug information ( '-g:vars' at a minimum). 

AmbiguousBindingException


When two pieces of advice defined in different aspects both need to run at the same join point,
unless you specify otherwise the order of execution is undefined. You can control the order of
execution by specifying precedence. 

org.springframework.core.Ordered 
Ordered.getValue()


@DeclareParents


@Aspect
public class UsageTracking {
  @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
  public static UsageTracked mixin;
  @Before("com.xyz.myapp.SystemArchitecture.businessService() && this(usageTracked)")
  public void recordUsage(UsageTracked usageTracked) {
    usageTracked.incrementUseCount();
  }
}


 possible to define aspects with alternate lifecycles :- Spring
supports AspectJ’s perthis and pertarget instantiation models ( percflow, percflowbelow,
and pertypewithin are not currently supported).

@Aspect("perthis(com.xyz.myapp.SystemArchitecture.businessService())")
public class MyAspect {
  private int someState;
  @Before(com.xyz.myapp.SystemArchitecture.businessService())
  public void recordServiceUsage() {
  // ...
  }
}

AutoProxyCreator
 
 <aop:config>
 <aop:aspect> 
 
<aop:config>
  <aop:aspect id="myAspect" ref="aBean">
  ...
  </aop:aspect>
</aop:config>
<bean id="aBean" class="...">
...
</bean>

<aop:config>
<aop:pointcut id="businessService"
expression="execution(* com.xyz.myapp.service.*.*(..))"/>
</aop:config>

<aop:config>
<aop:pointcut id="businessService"
expression="com.xyz.myapp.SystemArchitecture.businessService()"/>
</aop:config>


<aop:config>
<aop:aspect id="myAspect" ref="aBean">
<aop:pointcut id="businessService"
expression="execution(* com.xyz.myapp.service.*.*(..))"/>
...
</aop:aspect>
</aop:config>


<aop:config>
<aop:aspect id="myAspect" ref="aBean">
<aop:pointcut id="businessService"
expression="execution(* com.xyz.myapp.service.*.*(..)) &amp;&amp; this(service)"/>
<aop:before pointcut-ref="businessService" method="monitor"/>
...
</aop:aspect>
</aop:config>

When combining pointcut sub-expressions, '&&' is awkward within an XML document, and so the
keywords 'and', 'or' and 'not' can be used in place of '&&', '||' and '!' respectively. For example, the
previous pointcut may be better written as:
<aop:config>
<aop:aspect id="myAspect" ref="aBean">
<aop:pointcut id="businessService"
expression="execution(* com.xyz.myapp.service.*.*(..)) **and** this(service)"/>
<aop:before pointcut-ref="businessService" method="monitor"/>
...
</aop:aspect>
</aop:config>


<aop:aspect id="beforeExample" ref="aBean">
<aop:before
pointcut-ref="dataAccessOperation"
method="doAccessCheck"/>
...
</aop:aspect>


<aop:aspect id="beforeExample" ref="aBean">
<aop:before
pointcut="execution(* com.xyz.myapp.dao.*.*(..))"
method="doAccessCheck"/>
...
</aop:aspect>





the arg-names attribute of the advice element, which is treated in the same manner to the "argNames"
attribute in an advice annotation as described in the section called “Determining argument names”. For
example:
<aop:before
pointcut="com.xyz.lib.Pointcuts.anyPublicMethod() and @annotation(auditable)"
method="audit"
arg-names="auditable"/>




<!-- this is the object that will be proxied by Spring's AOP infrastructure -->
<bean id="fooService" class="x.y.service.DefaultFooService"/>
<!-- this is the actual advice itself -->
<bean id="profiler" class="x.y.SimpleProfiler"/>
<aop:config>
<aop:aspect ref="profiler">
<aop:pointcut id="theExecutionOfSomeFooServiceMethod"
expression="execution(* x.y.service.FooService.getFoo(String,int))
and args(name, age)"/>
<aop:around pointcut-ref="theExecutionOfSomeFooServiceMethod"
method="profile"/>
</aop:aspect>
</aop:config>



<aop:config proxy-target-class="true">
<!-- other beans defined here... -->
</aop:config>


public class Main {
public static void main(String[] args) {
ProxyFactory factory = new ProxyFactory(new SimplePojo());
factory.addInterface(Pojo.class);
factory.addAdvice(new RetryAdvice());
Pojo pojo = (Pojo) factory.getProxy();
// this is a method call on the proxy!
pojo.foo();
}
}







However, once the call has finally reached the target object, the
SimplePojo reference in this case, any method calls that it may make on itself, such as this.bar() or
this.foo(), are going to be invoked against the this reference, and not the proxy. This has important
implications. It means that self-invocation is not going to result in the advice associated with a method
invocation getting a chance to execute.


public class SimplePojo implements Pojo {
public void foo() {
// this works, but... gah!
((Pojo) AopContext.currentProxy()).bar();
}
}



public class Main {
public static void main(String[] args) {
ProxyFactory factory = new ProxyFactory(new SimplePojo());
factory.adddInterface(Pojo.class);
factory.addAdvice(new RetryAdvice());
factory.setExposeProxy(true);
Pojo pojo = (Pojo) factory.getProxy();
// this is a method call on the proxy!
pojo.foo();
}
}

org.springframework.aop.aspectj.annotation.AspectJProxyFactory

// create a factory that can generate a proxy for the given target object
AspectJProxyFactory factory = new AspectJProxyFactory(targetObject);
// add an aspect, the class must be an @AspectJ aspect
// you can call this as many times as you need with different aspects
factory.addAspect(SecurityManager.class);
// you can also add existing aspect instances, the type of the object supplied must be an @AspectJ
aspect
factory.addAspect(usageTracker);
// now get the proxy object...
MyInterfaceType proxy = factory.getProxy();

spring-aspects.jar

@Configurable

 Since the default name for a bean
is the fully-qualified name of its type, a convenient way to declare the prototype definition is simply to
omit the id attribute:
<bean class="com.xyz.myapp.domain.Account" scope="prototype">
<property name="fundsTransferService" ref="fundsTransferService"/>
</bean>

@Configurable("account") // by name 
@Configurable(autowire=Autowire.BY_NAME,dependencyCheck=true))

AnnotationBeanConfigurerAspect
@Configurable(preConstruction=true)
@EnableSpringConfigured
<context:spring-configured/>


<bean id="myService"
class="com.xzy.myapp.service.MyService"
depends-on="org.springframework.beans.factory.aspectj.AnnotationBeanConfigurerAspect">
<!-- ... -->
</bean>

Do not activate @Configurable processing through the bean configurer aspect unless you
really mean to rely on its semantics at runtime. In particular, make sure that you do not
use @Configurable on bean classes which are registered as regular Spring beans with the
container: You would get double initialization otherwise, once through the container and once
through the aspect.

AnnotationTransactionAspect.
AspectJ follows Java’s rule that annotations on
interfaces are not inherited.
JtaAnnotationTransactionAspect
AbstractBeanConfigurerAspect
AbstractTransactionAspect

public aspect DomainObjectConfiguration extends AbstractBeanConfigurerAspect {
  public DomainObjectConfiguration() {
  setBeanWiringInfoResolver(new ClassNameBeanWiringInfoResolver());
  }
  // the creation of a new bean (any object in the domain model)
  protected pointcut beanCreation(Object beanInstance) :
    initialization(new(..)) &&
    SystemArchitecture.inDomainModel() &&
    this(beanInstance);
}

<bean id="profiler" class="com.xyz.profiler.Profiler"
    factory-method="aspectOf">
    <property name="profilingStrategy" ref="jamonProfilingStrategy"/>
</bean>

<aop:aspectj-autoproxy>
  <aop:include name="thisBean"/>
  <aop:include name="thatBean"/>
</aop:aspectj-autoproxy>

Load-time weaving (LTW) 

<context:load-time-weaver/> 


@Aspect
public class ProfilingAspect {
  @Around("methodsToBeProfiled()")
  public Object profile(ProceedingJoinPoint pjp) throws Throwable {
    StopWatch sw = new StopWatch(getClass().getSimpleName());
    try {
    sw.start(pjp.getSignature().getName());
    return pjp.proceed();
    } finally {
    sw.stop();
    System.out.println(sw.prettyPrint());
    }
  }
  @Pointcut("execution(public * foo..*.*(..))")
  public void methodsToBeProfiled(){}

}

META-INF/aop.xml

<!DOCTYPE aspectj PUBLIC "-//AspectJ//DTD//EN" "http://www.eclipse.org/aspectj/dtd/aspectj.dtd">
<aspectj>
<weaver>
<!-- only weave classes in our application-specific packages -->
<include within="foo.*"/>
</weaver>
<aspects>
<!-- weave in just this aspect -->
<aspect name="foo.ProfilingAspect"/>
</aspects>
</aspectj>


java -javaagent:C:/projects/foo/lib/global/spring-instrument.jar foo.Main
InstrumentationSavingAgent,

The AspectJ LTW infrastructure is configured using one or more 'META-INF/aop.xml' files, that are on
the Java classpath (either directly, or more typically in jar files).

 spring-aop.jar 
 aspectjweaver.jar 
 spring-instrument.jar
LoadTimeWeaver
java.lang.instrument.ClassFileTransformers



@Configuration
@EnableLoadTimeWeaving
public class AppConfig {
}
<context:load-time-weaver/>

AspectJWeavingEnabler.
DefaultContextLoadTimeWeaver



WebLogicLoadTimeWeaver, GlassFishLoadTimeWeaver
TomcatLoadTimeWeaver
JBossLoadTimeWeaver
WebSphereLoadTimeWeaver
InstrumentationLoadTimeWeaver
InstrumentationSavingAgent
ReflectiveLoadTimeWeaver

<context:load-time-weaver
weaver-class="org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver"/>

ClassPreProcessorAgentAdapter
'aspectjWeaving'
TomcatInstrumentableClassLoader,

For performance reasons, it is recommended to use this configuration only if your target environment
(such as Jetty) does not have (or does not support) a dedicated LTW.


org.springframework.aop.Pointcut
ClassFilter
MethodMatcher
union and intersection.
ComposablePointcut
org.springframework.aop.aspectj.AspectJExpressionPointcut.
org.springframework.aop.support.JdkRegexpMethodPointcut


<bean id="settersAndAbsquatulatePointcut"
class="org.springframework.aop.support.JdkRegexpMethodPointcut">
  <property name="patterns">
    <list>
      <value>.*set.*</value>
      <value>.*absquatulate</value>
    </list>
  </property>
</bean>

RegexpMethodPointcutAdvisor,

<bean id="settersAndAbsquatulateAdvisor"
class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
  <property name="advice">
    <ref bean="beanNameOfAopAllianceInterceptor"/>
  </property>
  <property name="patterns">
    <list>
      <value>.*set.*</value>
      <value>.*absquatulate</value>
    </list>
  </property>
</bean>




Control flow pointcuts
A control flow pointcut matches the current call stack.
org.springframework.aop.support.ControlFlowPointcut
StaticMethodMatcherPointcut,
Interceptor
MethodInvocation

public interface MethodBeforeAdvice extends BeforeAdvice {
  void before(Method m, Object[] args, Object target) throws Throwable;
}

public class CountingBeforeAdvice implements MethodBeforeAdvice {
  private int count;
  public void before(Method m, Object[] args, Object target) throws Throwable {
    ++count;
  }
  public int getCount() {
    return count;
  }
}


org.springframework.aop.ThrowsAdvice

public class RemoteThrowsAdvice implements ThrowsAdvice {
  public void afterThrowing(RemoteException ex) throws Throwable {
  // Do something with remote exception
  }
}

public class ServletThrowsAdviceWithArguments implements ThrowsAdvice {
  public void afterThrowing(Method m, Object[] args, Object target, ServletException ex) {
  // Do something with all arguments
  }
}

public static class CombinedThrowsAdvice implements ThrowsAdvice {
public void afterThrowing(RemoteException ex) throws Throwable {
// Do something with remote exception
}
}
public void afterThrowing(Method m, Object[] args, Object target, ServletException ex) {
// Do something with all arguments
}

org.springframework.aop.AfterReturningAdvice


public class CountingAfterReturningAdvice implements AfterReturningAdvice {
  private int count;
  public void afterReturning(Object returnValue, Method m, Object[] args, Object target)
  throws Throwable {
    ++count;
  }
  public int getCount() {
    return count;
  }
}

public interface IntroductionInterceptor extends MethodInterceptor {
  boolean implementsInterface(Class intf);
}

public interface IntroductionAdvisor extends Advisor, IntroductionInfo {
  ClassFilter getClassFilter();
  void validateInterfaces() throws IllegalArgumentException;
}
public interface IntroductionInfo {
  Class[] getInterfaces();
}

org.springframework.aop.support.DelegatingIntroductionInterceptor
 suppressInterface(Class intf)


public class LockMixin extends DelegatingIntroductionInterceptor implements Lockable {
    private boolean locked;
    public void lock() {
    this.locked = true;
    }
    public void unlock() {
    this.locked = false;
    }
    public boolean locked() {
    return this.locked;
    }
    public Object invoke(MethodInvocation invocation) throws Throwable {
    if (locked() && invocation.getMethod().getName().indexOf("set") == 0) {
    throw new LockedException();
    }
    return super.invoke(invocation);
    }
}

public class LockMixinAdvisor extends DefaultIntroductionAdvisor {
  public LockMixinAdvisor() {
    super(new LockMixin(), Lockable.class);
  }
}

Advised.addAdvisor()

org.springframework.aop.support.DefaultPointcutAdvisor
org.springframework.aop.framework.ProxyFactoryBean.


org.springframework.aop.framework.ProxyConfig
proxyTargetClass: true 
optimize:
frozen:


exposeProxy: determines whether or not the current proxy should be exposed in a ThreadLocal
so that it can be accessed by the target. If a target needs to obtain the proxy and the exposeProxy
property is set to true, the target can use the AopContext.currentProxy() method.
proxyInterfaces:
interceptorNames:
singleton:
TransactionProxyFactoryBean




<bean id="personTarget" class="com.mycompany.PersonImpl">
<property name="name" value="Tony"/>
<property name="age" value="51"/>
</bean>
<bean id="myAdvisor" class="com.mycompany.MyAdvisor">
<property name="someProperty" value="Custom string property value"/>
</bean>
<bean id="debugInterceptor" class="org.springframework.aop.interceptor.DebugInterceptor">
</bean>
<bean id="person"
class="org.springframework.aop.framework.ProxyFactoryBean">
<property name="proxyInterfaces" value="com.mycompany.Person"/>
<property name="target" ref="personTarget"/>
<property name="interceptorNames">
<list>
<value>myAdvisor</value>
<value>debugInterceptor</value>
</list>
</property>
</bean>

Advised



<bean id="proxy" class="org.springframework.aop.framework.ProxyFactoryBean">
<property name="target" ref="service"/>
<property name="interceptorNames">
<list>
<value>global*</value>
</list>
</property>
</bean>
<bean id="global_debug" class="org.springframework.aop.interceptor.DebugInterceptor"/>
<bean id="global_performance" class="org.springframework.aop.interceptor.PerformanceMonitorInterceptor"/
>


<bean id="txProxyTemplate" abstract="true"
  class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
    <property name="transactionManager" ref="transactionManager"/>
    <property name="transactionAttributes">
      <props>
	<prop key="*">PROPAGATION_REQUIRED</prop>
      </props>
    </property>
  </bean>

  
  
  
  
<bean id="myService" parent="txProxyTemplate">
  <property name="target">
    <bean class="org.springframework.samples.MyServiceImpl">
    </bean>
  </property>
</bean>

  
  
  
  
<bean id="mySpecialService" parent="txProxyTemplate">
  <property name="target">
  <bean class="org.springframework.samples.MySpecialServiceImpl">
  </bean>
  </property>
  <property name="transactionAttributes">
    <props>
      <prop key="get*">PROPAGATION_REQUIRED,readOnly</prop>
      <prop key="find*">PROPAGATION_REQUIRED,readOnly</prop>
      <prop key="load*">PROPAGATION_REQUIRED,readOnly</prop>
      <prop key="store*">PROPAGATION_REQUIRED</prop>
    </props>
  </property>
</bean>

  
  
  
  
ProxyFactory factory = new ProxyFactory(myBusinessInterfaceImpl);
factory.addAdvice(myMethodInterceptor);
factory.addAdvisor(myAdvisor);
MyBusinessInterface tb = (MyBusinessInterface) factory.getProxy();

IntroductionInterceptionAroundAdvisor,

org.springframework.aop.framework.Advised interface. Any AOP proxy can be cast to this
interface, whichever other interfaces it implements. 
boolean isFrozen();

Advisor. Usually the advisor holding pointcut and
advice 

AopConfigException.
BeanNameAutoProxyCreator

<bean class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
  <property name="beanNames" value="jdk*,onlyJdk"/>
  <property name="interceptorNames">
    <list>
      <value>myInterceptor</value>
    </list>
  </property>
</bean>

DefaultAdvisorAutoProxyCreator.
TransactionProxyFactoryBean.
org.springframework.aop.TargetSource
HotSwappableTargetSource
The HotSwappableTargetSource is threadsafe.


HotSwappableTargetSource swapper = (HotSwappableTargetSource) beanFactory.getBean("swapper");
Object oldTarget = swapper.swap(newTarget);

<bean id="initialTarget" class="mycompany.OldTarget"/>
<bean id="swapper" class="org.springframework.aop.target.HotSwappableTargetSource">
<constructor-arg ref="initialTarget"/>
</bean>
<bean id="swappable" class="org.springframework.aop.framework.ProxyFactoryBean">
<property name="targetSource" ref="swapper"/>
</bean>

The above swap() call changes the target of the swappable bean. Clients who hold a reference to that
bean will be unaware of the change, but will immediately start hitting the new target.

org.springframework.aop.target.AbstractPoolingTargetSource 
PoolingTargetSource
org.springframework.aop.target.PoolingConfig

PoolingConfig conf = (PoolingConfig) beanFactory.getBean("businessObject");
System.out.println("Max pool size is " + conf.getMaxSize());

<bean id="prototypeTargetSource" class="org.springframework.aop.target.PrototypeTargetSource">
<property name="targetBeanName" ref="businessObjectTarget"/>
</bean>


<bean id="threadlocalTargetSource" class="org.springframework.aop.target.ThreadLocalTargetSource">
<property name="targetBeanName" value="businessObjectTarget"/>
</bean>

org.aopalliance.aop.Advice

MockEnvironment
MockPropertySource
ReflectionTestUtils
AopTestUtils
AopUtils
AopProxyUtils.
ModelAndViewAssert
ModelAndView
MockHttpServletRequest,
MockHttpSession,
TestContext

PlatformTransactionManager
@Commit


JdbcTestUtils,
AbstractTransactionalJUnit4SpringContextTests
AbstractTransactionalTestNGSpringContextTests 
spring-jdbc 
TestContext
@ContextConfiguration

@ContextConfiguration("/test-config.xml")
public class XmlApplicationContextTests {
// class body...
}

@ContextConfiguration(classes = TestConfig.class)
public class ConfigClassApplicationContextTests {
// class body...
}

@ContextConfiguration(initializers = CustomContextIntializer.class)
public class ContextInitializerTests {
// class body...
}

@ContextConfiguration(locations = "/test-context.xml", loader = CustomContextLoader.class)
public class CustomLoaderXmlApplicationContextTests {
// class body...
}

@WebAppConfiguration
MockServletContext

@ContextConfiguration
@WebAppConfiguration("classpath:test-web-resources")
public class WebAppTests {
// class body...
}
@ContextHierarchy


@ContextHierarchy({
@ContextConfiguration("/parent-config.xml"),
@ContextConfiguration("/child-config.xml")
})
public class ContextHierarchyTests {
// class body...
}



@WebAppConfiguration
@ContextHierarchy({
@ContextConfiguration(classes = AppConfig.class),
@ContextConfiguration(classes = WebConfig.class)
})
public class WebIntegrationTests {
// class body...
}

@ActiveProfiles

@ContextConfiguration
@ActiveProfiles("dev")
public class DeveloperTests {
// class body...
}

@ContextConfiguration
@ActiveProfiles({"dev", "integration"})
public class DeveloperIntegrationTests {
// class body...
}
ActiveProfilesResolver
@TestPropertySource


@ContextConfiguration
@TestPropertySource("/test.properties")
public class MyIntegrationTests {
// class body...
}

@ContextConfiguration
@TestPropertySource(properties = { "timezone = GMT", "port: 4242" })
public class MyIntegrationTests {
// class body...
}

@DirtiesContext

@DirtiesContext(classMode = BEFORE_CLASS)
public class FreshContextTests {
// some tests that require a new Spring container
}

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class FreshContextTests {
// some tests that require a new Spring container
}
AFTER_EACH_TEST_METHOD)
AFTER_CLASS

@DirtiesContext(methodMode = BEFORE_METHOD)
@Test
public void testProcessWhichRequiresFreshAppCtx() {
// some logic that requires a new Spring container
}
AFTER_METHOD


@ContextHierarchy({
@ContextConfiguration("/parent-config.xml"),
@ContextConfiguration("/child-config.xml")
})
public class BaseTests {
// class body...
}
public class ExtendedTests extends BaseTests {
}
@Test
@DirtiesContext(hierarchyMode = CURRENT_LEVEL)
public void test() {
// some logic that results in the child context being dirtied
}
DirtiesContext.HierarchyMode 

@TestExecutionListeners
TestContextManager.

@ContextConfiguration
@TestExecutionListeners({CustomTestExecutionListener.class, AnotherTestExecutionListener.class})
public class CustomTestExecutionListenerTests {
// class body...
}


public class BeforeClassHook extends AbstractTestExecutionListener {

    public BeforeClassHook() { }

    @Override
    public void beforeTestClass(TestContext testContext) {
        System.out.println("BeforeClassHook.beforeTestClass(): set up.");
    }
}


@Commit
@Rollback(false) 

@BeforeTransaction
@AfterTransaction

@Sql
@Test
@Sql({"/test-schema.sql", "/test-user-data.sql"})
public void userTest {
// execute code that relies on the test schema and test data
}

@SqlConfig


@Test
@Sql(
scripts = "/test-user-data.sql",
config = @SqlConfig(commentPrefix = "`", separator = "@@")
)
public void userTest {
// execute code that relies on the test data
}
@SqlGroup

@Test
@SqlGroup({
@Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`")),
@Sql("/test-user-data.sql")
)}
public void userTest {
// execute code that uses the test schema and test data
}


• @Autowired
• @Qualifier
• @Resource (javax.annotation) if JSR-250 is present
• @Inject (javax.inject) if JSR-330 is present
• @Named (javax.inject) if JSR-330 is present
• @PersistenceContext (javax.persistence) if JPA is present
• @PersistenceUnit (javax.persistence) if JPA is present
• @Required
• @Transactional

SpringJUnit4ClassRunner,
@IfProfileValue
ProfileValueSource

@IfProfileValue(name="java.vendor", value="Oracle Corporation")
@Test
public void testProcessWhichRunsOnlyOnOracleJvm() {
// some logic that should run only on Java VMs from Oracle Corporation
}

@IfProfileValue(name="test-groups", values={"unit-tests", "integration-tests"})
@ProfileValueSourceConfiguration
SystemProfileValueSource

@ProfileValueSourceConfiguration(CustomProfileValueSource.class)
public class CustomProfileValueSourceTests {
// class body...
}

@Timed

@Timed(millis=1000)
public void testProcessWithOneSecondTimeout() {
// some logic that should not take longer than 1 second to execute
}

JUnit’s @Test(timeout=...)


@Repeat
@Repeat(10)

• @ContextConfiguration
• @ContextHierarchy
• @ActiveProfiles
• @TestPropertySource
• @DirtiesContext
• @WebAppConfiguration
• @TestExecutionListeners
• @Transactional
• @BeforeTransaction
• @AfterTransaction
• @Rollback
• @Sql
• @SqlConfig
• @SqlGroup
• @Repeat
• @Timed
• @IfProfileValue
• @ProfileValueSourceConfiguration


TestContext
TestContextManager
TestExecutionListener,
ContextLoader,
SmartContextLoader
DelegatingSmartContextLoader:	
AnnotationConfigContextLoader,
GenericXmlContextLoader,
GenericGroovyXmlContextLoader
WebDelegatingSmartContextLoader:
AnnotationConfigWebContextLoader,
GenericXmlWebContextLoader,
GenericGroovyXmlWebContextLoader
GenericPropertiesContextLoade


ServletTestExecutionListener:
DependencyInjectionTestExecutionListener:
DirtiesContextTestExecutionListener:
TransactionalTestExecutionListener:
SqlScriptsTestExecutionListener:
SpringFactoriesLoader
META-INF/spring.factories 
AnnotationAwareOrderComparator




@ContextConfiguration
@TestExecutionListeners({
  MyCustomTestExecutionListener.class,
  ServletTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  SqlScriptsTestExecutionListener.class
})
public class MyTest {
// class body...
}



@ContextConfiguration
@TestExecutionListeners(
listeners = MyCustomTestExecutionListener.class,
mergeMode = MERGE_WITH_DEFAULTS
)
public class MyTest {
// class body...
}

AbstractJUnit4SpringContextTests
AbstractTestNGSpringContextTests

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from "/app-config.xml" and
// "/test-config.xml" in the root of the classpath
@ContextConfiguration(locations={"/app-config.xml", "/test-config.xml"})
public class MyTest {
// class body...
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/app-config.xml", "/test-config.xml"})
public class MyTest {
// class body...
}
GenericXmlContextLoader
GenericXmlWebContextLoader


If your class is named com.example.MyTest,
GenericXmlContextLoader loads your application context from "classpath:com/example/
MyTest-context.xml".

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from
// "classpath:com/example/MyTest-context.xml"
@ContextConfiguration
public class MyTest {
// class body...
}




@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from "/AppConfig.groovy" and
// "/TestConfig.groovy" in the root of the classpath
@ContextConfiguration({"/AppConfig.groovy", "/TestConfig.Groovy"})
public class MyTest {
// class body...
}


GenericGroovyXmlContextLoader
GenericGroovyXmlWebContextLoader


package com.example;
@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from
// "classpath:com/example/MyTestContext.groovy"
@ContextConfiguration
public class MyTest {
// class body...
}


AnnotationConfigContextLoader
AnnotationConfigWebContextLoader


AnnotationConfigContextLoader and AnnotationConfigWebContextLoader will detect all
static nested classes of the test class that meet the requirements for configuration class
implementations as specified in the @Configuration javadocs.



@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the
// static nested Config class
@ContextConfiguration
public class OrderServiceTest {
@Configuration
static class Config {
}
// this bean will be injected into the OrderServiceTest class
@Bean
public OrderService orderService() {
OrderService orderService = new OrderServiceImpl();
// set properties, etc.
return orderService;
}
@Autowired
private OrderService orderService;
@Test
public void testOrderService() {
// test the orderService
}
}


ApplicationContextInitializer.

@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from TestConfig
// and initialized by TestAppCtxInitializer
@ContextConfiguration(
classes = TestConfig.class,
initializers = TestAppCtxInitializer.class)
public class MyTest {
// class body...
}

@ContextConfiguration
inheritLocations
inheritInitializers

Beans within the default profile are only
included when no other profile is specifically activated.
This can be used to define fallback beans to be
used in the application’s default state. 

@Configuration
@Profile("default")
public class DefaultDataConfig {
}
@Bean
public DataSource dataSource() {
return new EmbeddedDatabaseBuilder()
.setType(EmbeddedDatabaseType.HSQL)
.addScript("classpath:com/bank/config/sql/schema.sql")
.build();
}





package com.bank.service;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
TransferServiceConfig.class,
StandaloneDataConfig.class,
JndiDataConfig.class,
DefaultDataConfig.class})
@ActiveProfiles("dev")
public class TransferServiceTest {
@Autowired
private TransferService transferService;
}
@Test
public void testTransferService() {
// test the transferService
}



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
TransferServiceConfig.class,
StandaloneDataConfig.class,
JndiDataConfig.class,
DefaultDataConfig.class})
@ActiveProfiles("dev")
public abstract class AbstractIntegrationTest {
}




// "dev" profile overridden with "production"
@ActiveProfiles(profiles = "production", inheritProfiles = false)
public class ProductionTransferServiceTest extends AbstractIntegrationTest {
// test body
}

ActiveProfilesResolver
OperatingSystemActiveProfilesResolver.


// "dev" profile overridden programmatically via a custom resolver
@ActiveProfiles(
resolver = OperatingSystemActiveProfilesResolver.class,
inheritProfiles = false)
public class TransferServiceTest extends AbstractIntegrationTest {
// test body
}


public class OperatingSystemActiveProfilesResolver implements ActiveProfilesResolver {
}
@Override
String[] resolve(Class<?> testClass) {
String profile = ...;
// determine the value of profile based on the operating system
return new String[] {profile};
}

Both traditional and XML-based properties file formats are supported — for example, "classpath:/
com/example/test.properties" or "file:///path/to/file.xml".

Resource location wildcards (e.g. */.properties) are not permitted: each location must evaluate to exactly one
.properties or .xml resource.

• "key=value"
• "key:value"
• "key value"
@ContextConfiguration
@TestPropertySource(properties = {"timezone = GMT", "port: 4242"})
public class MyIntegrationTests {
// class body...
}

 @TestPropertySource is declared as an empty annotation 
com.example.MyTest, the corresponding default properties file is "classpath:com/example/
MyTest.properties". If the default cannot be detected, an IllegalStateException will be
thrown.



@ContextConfiguration
@TestPropertySource(
locations = "/test.properties",
properties = {"timezone = GMT", "port: 4242"}
)
public class MyIntegrationTests {
// class body...
}


@TestPropertySource supports boolean inheritLocations and inheritProperties
attributes that denote whether resource locations for properties files and inlined properties declared by
superclasses should be inherited. 

@TestPropertySource(properties = "key1 = value1")

ServletTestExecutionListener

Note that the WebApplicationContext and MockServletContext are
both cached across the test suite; whereas, the other mocks are managed per test method by the
ServletTestExecutionListener.

The Spring TestContext framework stores application contexts in a static cache. This means that
the context is literally stored in a static variable. 
forkMode for the Maven Surefire plug-in is set to always or pertes
org.springframework.test.context.cache logging category to DEBUG.


@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
@ContextConfiguration(name = "parent", locations = "/app-config.xml"),
@ContextConfiguration(name = "child", locations = "/user-config.xml")
})
public class BaseTests {}
@ContextHierarchy(
@ContextConfiguration(name = "child", locations = "/order-config.xml")
)
public class ExtendedTests extends BaseTests {}


@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
@ContextConfiguration(name = "parent", locations = "/app-config.xml"),
@ContextConfiguration(name = "child", locations = "/user-config.xml")
})
public class BaseTests {}
@ContextHierarchy(
@ContextConfiguration(
name = "child",
locations = "/test-user-config.xml",
inheritLocations = false
))
public class ExtendedTests extends BaseTests {}


@Autowired
@Override
public void setDataSource(@Qualifier("myDataSource") DataSource dataSource) {
super.setDataSource(dataSource);
}
Its value is matched against <qualifier> declarations within
the corresponding <bean> definitions. The bean name is used as a fallback qualifier value


<beans>
  <bean id="userService"
    class="com.example.SimpleUserService"
    c:loginAction-ref="loginAction" />
  <bean id="loginAction" class="com.example.LoginAction"
    c:username="{request.getParameter(''user'')}"
    c:password="{request.getParameter(''pswd'')}"
    scope="request">
    <aop:scoped-proxy />
  </bean>
</beans>





@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class RequestScopedBeanTests {
@Autowired UserService userService;
@Autowired MockHttpServletRequest request;
@Test
public void requestScope() {
request.setParameter("user", "enigma");
request.setParameter("pswd", "$pr!ng");
LoginResults results = userService.loginUser();
// assert results
}
}



<beans>
  <beanid="userService"
    class="com.example.SimpleUserService"
    c:userPreferences-ref="userPreferences"/>
  <bean id="userPreferences"
    class="com.example.UserPreferences"
    c:theme="#{session.getAttribute(''theme'')}"
    scope="session">
    <aop:scoped-proxy />
  </bean>
</beans>


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class SessionScopedBeanTests {
  @Autowired UserService userService;
  @Autowired MockHttpSession session;
  @Test
  public void sessionScope() throws Exception {
    session.setAttribute("theme", "blue");
    Results results = userService.processUserPreferences();
  // assert results
  }
}


Annotating a test method with @Transactional causes the test to be run within a transaction that
will, by default, be automatically rolled back after completion of the test.

NOT_SUPPORTED
AbstractTransactionalJUnit4SpringContextTests
AbstractTransactionalTestNGSpringContextTests 


TestTransaction


@ContextConfiguration(classes = TestConfig.class)
public class ProgrammaticTransactionManagementTests extends
AbstractTransactionalJUnit4SpringContextTests {
  @Test
  public void transactionalTest() {
  // assert initial state in test database:
  assertNumUsers(2);
  deleteFromTables("user");
  // changes to the database will be committed!
  TestTransaction.flagForCommit();
  TestTransaction.end();
  assertFalse(TestTransaction.isActive());
  assertNumUsers(0);
  }
  TestTransaction.start();
  // perform other actions against the database that will
  // be automatically rolled back after the test completes...
}

Any before methods (such as methods annotated with JUnit’s @Before) and any after methods
(such as methods annotated with JUnit’s @After) are executed within a transaction. In addition,
methods annotated with @BeforeTransaction or @AfterTransaction are naturally not
executed for test methods that are not configured to run within a transaction.

@Transactional("myTxMgr") 
@Transactional(transactionManager = "myTxMgr")
TestContextTransactionUtils.retrieveTransactionManager() 



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(transactionManager = "txMgr")
@Commit
public class FictitiousTransactionalTest {
  @BeforeTransaction
  public void verifyInitialDatabaseState() {
  // logic to verify the initial state before a transaction is started
  }
  @Before
  public void setUpTestDataWithinTransaction() {
  // set up test data within the transaction
  }
  @Test
  // overrides the class-level default rollback setting
  @Rollback
  public void modifyDatabaseWithinTransaction() {
  // logic which uses the test data and modifies database state
  }
  @After
  public void tearDownWithinTransaction() {
  // execute "tear down" logic within the transaction
  }
  @AfterTransaction
  public void verifyFinalDatabaseState() {
  // logic to verify the final state after transaction has rolled back
  }
}

When you test application code that manipulates the state of the Hibernate session, make sure
to flush the underlying session within test methods that execute that code. Failing to flush the
underlying session can produce false positives: your test may pass, but the same code throws an
exception in a live, production environment.

@Autowired
private SessionFactory sessionFactory;
@Test // no expected exception!
public void falsePositive() {
  updateEntityInHibernateSession();
  // False positive: an exception will be thrown once the session is
  // finally flushed (i.e., in production code)
  }
  @Test(expected = GenericJDBCException.class)
  public void updateWithSessionFlush() {
  updateEntityInHibernateSession();
  // Manual flush is required to avoid false positive in test
  sessionFactory.getCurrentSession().flush();
  }

org.springframework.jdbc.datasource.init.ScriptUtils
org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests



@Test
public void databaseTest {
  ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
  populator.addScripts(
  new ClassPathResource("test-schema.sql"),
  new ClassPathResource("test-data.sql"));
  populator.setSeparator("@@");
  populator.execute(this.dataSource);
  // execute code that uses the test schema and data
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Sql("/test-schema.sql")
public class DatabaseTests {
@Test
public void emptySchemaTest {
// execute code that uses the test schema without any test data
}
}
@Test
@Sql({"/test-schema.sql", "/test-user-data.sql"})
public void userTest {
// execute code that uses the test schema and test data
}

• class-level declaration: if the annotated test class is com.example.MyTest, the corresponding
default script is "classpath:com/example/MyTest.sql".
• method-level declaration: if the annotated test method is named testMethod() and is definedthe class com.example.MyTest, the corresponding default script is "classpath:com/example/
MyTest.testMethod.sql".



@Test
@Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`"))
@Sql("/test-user-data.sql")
public void userTest {
// execute code that uses the test schema and test data
}


@Test
@SqlGroup({
@Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`")),
@Sql("/test-user-data.sql")
)}
public void userTest {
// execute code that uses the test schema and test data
}


ISOLATED
AFTER_TEST_METHOD


@Test
@Sql(
scripts = "create-test-data.sql",
config = @SqlConfig(transactionMode = ISOLATED)
)
@Sql(
scripts = "delete-test-data.sql",
config = @SqlConfig(transactionMode = ISOLATED),
executionPhase = AFTER_TEST_METHOD
)
public void userTest {
// execute code that needs the test data to be committed
// to the database outside of the test's transaction
}


 unfortunately not possible to assign a value of
null to an annotation attribute. Thus, in order to support overrides of inherited global configuration,
@SqlConfig attributes have an explicit default value of either "" for Strings or DEFAULT for Enums
<jdbc:initialize-database/> 
Parameterized
MockitoJUnitRunner
SpringClassRule
SpringMethodRule

In contrast to the SpringJUnit4ClassRunner, Spring’s rule-based JUnit support has the advantage
that it is independent of any org.junit.runner.Runner implementation and can therefore be
combined with existing alternative runners like JUnit’s Parameterized or third-party runners such as
the MockitoJUnitRunner.

// Optionally specify a non-Spring Runner via @RunWith(...)
@ContextConfiguration
public class IntegrationTest {
  @ClassRule
  public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
  @Rule
  public final SpringMethodRule springMethodRule = new SpringMethodRule();
  @Test
  public void testMethod() {
  // execute test logic...
  }
}

AbstractJUnit4SpringContextTests
AbstractTransactionalJUnit4SpringContextTests

This class expects a javax.sql.DataSource bean and a
PlatformTransactionManager bean to be defined in the ApplicationContext. 
executeSqlScript(..)

AbstractTestNGSpringContextTests
AbstractTransactionalTestNGSpringContextTests




 simple query for getting the number of rows in a relation:
int rowCount = this.jdbcTemplate.queryForObject("select count(*) from t_actor", Integer.class);

A simple query using a bind variable:
int countOfActorsNamedJoe = this.jdbcTemplate.queryForObject(
"select count(*) from t_actor where first_name = ?", Integer.class, "Joe");

Querying for a String:
String lastName = this.jdbcTemplate.queryForObject(
"select last_name from t_actor where id = ?",
new Object[]{1212L}, String.class);


Actor actor = this.jdbcTemplate.queryForObject(
"select first_name, last_name from t_actor where id = ?",
new Object[]{1212L},
new RowMapper<Actor>() {
public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
  Actor actor = new Actor();
  actor.setFirstName(rs.getString("first_name"));
  actor.setLastName(rs.getString("last_name"));
  return actor;
}
});



this.jdbcTemplate.update(
"insert into t_actor (first_name, last_name) values (?, ?)",
"Leonor", "Watling");

this.jdbcTemplate.update(
"update t_actor set last_name = ? where id = ?",
"Banjo", 5276L);

this.jdbcTemplate.update(
"delete from actor where id = ?",
Long.valueOf(actorId));


this.jdbcTemplate.execute("create table mytable (id integer, name varchar(100))");

this.jdbcTemplate.update(
"call SUPPORT.REFRESH_ACTORS_SUMMARY(?)",
Long.valueOf(unionId));

Instances of the JdbcTemplate class are threadsafe once configured. 
JdbcTemplate
NamedParameterJdbcTemplate



public class JdbcCorporateEventDao implements CorporateEventDao {
  private JdbcTemplate jdbcTemplate;
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }
  // JDBC-backed implementations of the methods on the CorporateEventDao follow...
}


JdbcDaoSupport
String sql = "select count(*) from T_ACTOR where first_name = :first_name";
SqlParameterSource namedParameters = new MapSqlParameterSource("first_name", firstName);
Map<String, String> namedParameters = Collections.singletonMap("first_name", firstName);
BeanPropertySqlParameterSource
SQLExceptionTranslator
SQLErrorCodeSQLExceptionTranslator
SQLErrorCodes
SQLErrorCodesFactory
sql-error-codes.xml
DatabaseProductName
DatabaseMetaData
CustomSQLErrorCodesTranslation

public class CustomSQLErrorCodesTranslator extends SQLErrorCodeSQLExceptionTranslator {
  
  protected DataAccessException customTranslate(String task, String sql, SQLException sqlex) {
    if (sqlex.getErrorCode() == -12345) {
      return new DeadlockLoserDataAccessException(task, sqlex);
    }
    return null;
}}

InvalidDataAccessApiUsageException
queryForObject(..).
queryForList(..)

An update() convenience method supports the retrieval of primary keys generated by the database
PreparedStatementCreator
KeyHolder
PreparedStatement



final String INSERT_SQL = "insert into my_test (name) values(?)";
final String name = "Rob";
KeyHolder keyHolder = new GeneratedKeyHolder();
jdbcTemplate.update(
  new PreparedStatementCreator() {
  public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
  PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
  ps.setString(1, name);
  return ps;
  }
},
keyHolder);

Apache Jakarta Commons DBCP and C3P0
DriverManagerDataSource

Only use the DriverManagerDataSource class should only be used for testing purposes since
it does not provide pooling and will perform poorly when multiple requests for a connection are
made.


<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
  <property name="driverClassName" value="${jdbc.driverClassName}"/>
  <property name="url" value="${jdbc.url}"/>
  <property name="username" value="${jdbc.username}"/>
  <property name="password" value="${jdbc.password}"/>
</bean>

<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
  <property name="driverClass" value="${jdbc.driverClassName}"/>
  <property name="jdbcUrl" value="${jdbc.url}"/>
  <property name="user" value="${jdbc.username}"/>
  <property name="password" value="${jdbc.password}"/>
</bean>

DataSourceUtils
SmartDataSource
AbstractDataSource
SingleConnectionDataSource

The SingleConnectionDataSource class is an implementation of the SmartDataSource interface
that wraps a single Connection that is not closed after each use. Obviously, this is not multi-threading
capable.

DriverManagerDataSource
The DriverManagerDataSource class is an implementation of the standard DataSource interface
that configures a plain JDBC driver through bean properties, and returns a new Connection every time

page 406

TransactionAwareDataSourceProxy
TransactionAwareDataSourceProxy is a proxy for a target DataSource, which wraps that target
DataSource to add awareness of Spring-managed transactions


DataSourceTransactionManager
The DataSourceTransactionManager class is a PlatformTransactionManager
implementation for single JDBC datasources. It binds a JDBC connection from the specified data source
to the currently executing thread, potentially allowing for one thread connection per data source.


DataSourceUtils.getConnection(DataSource)
Connection
Statement
ResultSet 
JdbcTemplate
OracleLobHandler
NativeJdbcExtractor


SimpleNativeJdbcExtractor
C3P0NativeJdbcExtractor
CommonsDbcpNativeJdbcExtractor
JBossNativeJdbcExtractor
WebLogicNativeJdbcExtractor
WebSphereNativeJdbcExtractor
XAPoolNativeJdbcExtractor

BatchPreparedStatementSetter
InterruptibleBatchPreparedStatementSetter
NamedParameterJdbcTemplate
SqlParameterSource

public class JdbcActorDao implements ActorDao {
	private NamedParameterTemplate namedParameterJdbcTemplate;
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	public int[] batchUpdate(final List<Actor> actors) {
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(actors.toArray());
		int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(
		"update t_actor set first_name = :firstName, last_name = :lastName where id = :id",
		batch);
		return updateCounts;
	}
	// ... additional methods
}



public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public int[] batchUpdate(final List<Actor> actors) {
		List<Object[]> batch = new ArrayList<Object[]>();
		for (Actor actor : actors) {
		Object[] values = new Object[] {
		actor.getFirstName(),
		actor.getLastName(),
		actor.getId()};
		batch.add(values);
	}
	int[] updateCounts = jdbcTemplate.batchUpdate(
		"update t_actor set first_name = ?, last_name = ? where id = ?",
		batch);
	return updateCounts;
	}
	// ... additional methods
}

ParameterizedPreparedStatementSetter

SimpleJdbcInsert
SimpleJdbcCall




public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertActor;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertActor = new SimpleJdbcInsert(dataSource).withTableName("t_actor");
	}
	public void add(Actor actor) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);
		parameters.put("id", actor.getId());
		parameters.put("first_name", actor.getFirstName());
		parameters.put("last_name", actor.getLastName());
		insertActor.execute(parameters);
	}
	// ... additional methods
}



public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertActor;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertActor = new SimpleJdbcInsert(dataSource)
		.withTableName("t_actor")
		.usingGeneratedKeyColumns("id");
	}
	public void add(Actor actor) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("first_name", actor.getFirstName());
		parameters.put("last_name", actor.getLastName());
		Number newId = insertActor.executeAndReturnKey(parameters);
		actor.setId(newId.longValue());
	}
	// ... additional methods
}

KeyHolder

public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertActor;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertActor = new SimpleJdbcInsert(dataSource)
		.withTableName("t_actor")
		.usingColumns("first_name", "last_name")
		.usingGeneratedKeyColumns("id");
	}
	public void add(Actor actor) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
		parameters.put("first_name", actor.getFirstName());
		parameters.put("last_name", actor.getLastName());
		Number newId = insertActor.executeAndReturnKey(parameters);
		actor.setId(newId.longValue());
	}
	// ... additional methods
}

SqlParameterSource
BeanPropertySqlParameterSource

public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertActor;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertActor = new SimpleJdbcInsert(dataSource)
		.withTableName("t_actor")
		.usingGeneratedKeyColumns("id");
	}
	public void add(Actor actor) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(actor);
		Number newId = insertActor.executeAndReturnKey(parameters);
		actor.setId(newId.longValue());
	}
	// ... additional methods
}

MapSqlParameterSource

public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertActor;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.insertActor = new SimpleJdbcInsert(dataSource)
		.withTableName("t_actor")
		.usingGeneratedKeyColumns("id");
	}
	public void add(Actor actor) {
		SqlParameterSource parameters = new MapSqlParameterSource()
		.addValue("first_name", actor.getFirstName())
		.addValue("last_name", actor.getLastName());
		Number newId = insertActor.executeAndReturnKey(parameters);
		actor.setId(newId.longValue());
	}
	// ... additional methods
}





CREATE PROCEDURE read_actor (
	IN in_id INTEGER,
	OUT out_first_name VARCHAR(100),
	OUT out_last_name VARCHAR(100),
	OUT out_birth_date DATE)
BEGIN
	SELECT first_name, last_name, birth_date
	INTO out_first_name, out_last_name, out_birth_date
	FROM t_actor where id = in_id;
END;



public class JdbcActorDao implements ActorDao {
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall procReadActor;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.procReadActor = new SimpleJdbcCall(dataSource)
		.withProcedureName("read_actor");
	}
	public Actor readActor(Long id) {
		SqlParameterSource in = new MapSqlParameterSource()
		.addValue("in_id", id);
		Map out = procReadActor.execute(in);
		Actor actor = new Actor();
		actor.setId(id);
		actor.setFirstName((String) out.get("out_first_name"));
		actor.setLastName((String) out.get("out_last_name"));
		actor.setBirthDate((Date) out.get("out_birth_date"));
		return actor;
	}
	// ... additional methods
}


LinkedCaseInsensitiveMap
setResultsMapCaseInsensitive property to true


public class JdbcActorDao implements ActorDao {
	private SimpleJdbcCall procReadActor;
	public void setDataSource(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
		this.procReadActor = new SimpleJdbcCall(jdbcTemplate)
		.withProcedureName("read_actor");
	}
	// ... additional methods
}

page 415






