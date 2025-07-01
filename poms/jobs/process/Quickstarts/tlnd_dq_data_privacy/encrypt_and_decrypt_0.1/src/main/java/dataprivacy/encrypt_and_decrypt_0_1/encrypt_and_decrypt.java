
package dataprivacy.encrypt_and_decrypt_0_1;

import routines.DataOperation;
import routines.TalendDataGenerator;
import routines.DataQuality;
import routines.Relational;
import routines.Mathematical;
import routines.DataQualityDependencies;
import routines.SQLike;
import routines.Numeric;
import routines.TalendStringUtil;
import routines.TalendString;
import routines.MDM;
import routines.StringHandling;
import routines.DQTechnical;
import routines.TalendDate;
import routines.DataMasking;
import routines.DqStringHandling;
import routines.system.*;
import routines.system.api.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Comparator;

@SuppressWarnings("unused")

/**
 * Job: encrypt_and_decrypt Purpose: <br>
 * Description: <br>
 * 
 * @author Habushi, Ofer
 * @version 8.0.1.20250625_0954-patch
 * @status
 */
public class encrypt_and_decrypt implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "encrypt_and_decrypt.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(encrypt_and_decrypt.class);

	static {
		String javaUtilLoggingConfigFile = System.getProperty("java.util.logging.config.file");
		if (javaUtilLoggingConfigFile == null) {
			setupDefaultJavaUtilLogging();
		}
	}

	/**
	 * This class replaces the default {@code System.err} stream used by Java Util
	 * Logging (JUL). You can use your own configuration through the
	 * {@code java.util.logging.config.file} system property, enabling you to
	 * specify an external logging configuration file for tailored logging setup.
	 */
	public static class StandardConsoleHandler extends java.util.logging.StreamHandler {
		public StandardConsoleHandler() {
			// Set System.out as default log output stream
			super(System.out, new java.util.logging.SimpleFormatter());
		}

		/**
		 * Publish a {@code LogRecord}. The logging request was made initially to a
		 * {@code Logger} object, which initialized the {@code LogRecord} and forwarded
		 * it here.
		 *
		 * @param record description of the log event. A null record is silently ignored
		 *               and is not published
		 */
		@Override
		public void publish(java.util.logging.LogRecord record) {
			super.publish(record);
			flush();
		}

		/**
		 * Override {@code StreamHandler.close} to do a flush but not to close the
		 * output stream. That is, we do <b>not</b> close {@code System.out}.
		 */
		@Override
		public void close() {
			flush();
		}
	}

	protected static void setupDefaultJavaUtilLogging() {
		java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();

		// Get the root logger
		java.util.logging.Logger rootLogger = logManager.getLogger("");

		// Remove existing handlers to set standard console handler only
		java.util.logging.Handler[] handlers = rootLogger.getHandlers();
		for (java.util.logging.Handler handler : handlers) {
			rootLogger.removeHandler(handler);
		}

		rootLogger.addHandler(new StandardConsoleHandler());
		rootLogger.setLevel(java.util.logging.Level.INFO);
	}

	protected static boolean isCBPClientPresent() {
		boolean isCBPClientPresent = false;
		try {
			Class.forName("org.talend.metrics.CBPClient");
			isCBPClientPresent = true;
		} catch (java.lang.ClassNotFoundException e) {
		}
		return isCBPClientPresent;
	}

	protected static void logIgnoredError(String message, Throwable cause) {
		log.error(message, cause);

	}

	public final Object obj = new Object();

	// for transmiting parameters purpose
	private Object valueObject = null;

	public Object getValueObject() {
		return this.valueObject;
	}

	public void setValueObject(Object valueObject) {
		this.valueObject = valueObject;
	}

	private final static String defaultCharset = java.nio.charset.Charset.defaultCharset().name();

	private final static String utf8Charset = "UTF-8";

	public static String taskExecutionId = null;

	public static String jobExecutionId = java.util.UUID.randomUUID().toString();;

	private final static boolean isCBPClientPresent = isCBPClientPresent();

	// contains type for every context property
	public class PropertiesWithType extends java.util.Properties {
		private static final long serialVersionUID = 1L;
		private java.util.Map<String, String> propertyTypes = new java.util.HashMap<>();

		public PropertiesWithType(java.util.Properties properties) {
			super(properties);
		}

		public PropertiesWithType() {
			super();
		}

		public void setContextType(String key, String type) {
			propertyTypes.put(key, type);
		}

		public String getContextType(String key) {
			return propertyTypes.get(key);
		}
	}

	// create and load default properties
	private java.util.Properties defaultProps = new java.util.Properties();

	// create application properties with default
	public class ContextProperties extends PropertiesWithType {

		private static final long serialVersionUID = 1L;

		public ContextProperties(java.util.Properties properties) {
			super(properties);
		}

		public ContextProperties() {
			super();
		}

		public void synchronizeContext() {

			if (CLIENTS_FILE_LOC != null) {

				this.setProperty("CLIENTS_FILE_LOC", CLIENTS_FILE_LOC.toString());

			}

			if (CyrptoFilePath != null) {

				this.setProperty("CyrptoFilePath", CyrptoFilePath.toString());

			}

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
		}

		public String CLIENTS_FILE_LOC;

		public String getCLIENTS_FILE_LOC() {
			return this.CLIENTS_FILE_LOC;
		}

		public String CyrptoFilePath;

		public String getCyrptoFilePath() {
			return this.CyrptoFilePath;
		}
	}

	protected ContextProperties context = new ContextProperties(); // will be instanciated by MS.

	public ContextProperties getContext() {
		return this.context;
	}

	protected java.util.Map<String, String> defaultProperties = new java.util.HashMap<String, String>();
	protected java.util.Map<String, String> additionalProperties = new java.util.HashMap<String, String>();

	public java.util.Map<String, String> getDefaultProperties() {
		return this.defaultProperties;
	}

	public java.util.Map<String, String> getAdditionalProperties() {
		return this.additionalProperties;
	}

	private final String jobVersion = "0.1";
	private final String jobName = "encrypt_and_decrypt";
	private final String projectName = "DATAPRIVACY";
	public Integer errorCode = null;
	private String currentComponent = "";
	public static boolean isStandaloneMS = Boolean.valueOf("false");

	private void s(final String component) {
		try {
			org.talend.metrics.DataReadTracker.setCurrentComponent(jobName, component);
		} catch (Exception | NoClassDefFoundError e) {
			// ignore
		}
	}

	private void mdc(final String subJobName, final String subJobPidPrefix) {
		mdcInfo.forEach(org.slf4j.MDC::put);
		org.slf4j.MDC.put("_subJobName", subJobName);
		org.slf4j.MDC.put("_subJobPid", subJobPidPrefix + subJobPidCounter.getAndIncrement());
	}

	private void sh(final String componentId) {
		ok_Hash.put(componentId, false);
		start_Hash.put(componentId, System.currentTimeMillis());
	}

	{
		s("none");
	}

	private String cLabel = null;

	private final java.util.Map<String, Object> globalMap = new java.util.HashMap<String, Object>();
	private final static java.util.Map<String, Object> junitGlobalMap = new java.util.HashMap<String, Object>();

	private final java.util.Map<String, Long> start_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Long> end_Hash = new java.util.HashMap<String, Long>();
	private final java.util.Map<String, Boolean> ok_Hash = new java.util.HashMap<String, Boolean>();
	public final java.util.List<String[]> globalBuffer = new java.util.ArrayList<String[]>();

	private final JobStructureCatcherUtils talendJobLog = new JobStructureCatcherUtils(jobName,
			"_ERHsAKJjEemUjYpcVKCicA", "0.1");
	private org.talend.job.audit.JobAuditLogger auditLogger_talendJobLog = null;

	private RunStat runStat = new RunStat(talendJobLog, System.getProperty("audit.interval"));

	// OSGi DataSource
	private final static String KEY_DB_DATASOURCES = "KEY_DB_DATASOURCES";

	private final static String KEY_DB_DATASOURCES_RAW = "KEY_DB_DATASOURCES_RAW";

	public void setDataSources(java.util.Map<String, javax.sql.DataSource> dataSources) {
		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		for (java.util.Map.Entry<String, javax.sql.DataSource> dataSourceEntry : dataSources.entrySet()) {
			talendDataSources.put(dataSourceEntry.getKey(),
					new routines.system.TalendDataSource(dataSourceEntry.getValue()));
		}
		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	public void setDataSourceReferences(List serviceReferences) throws Exception {

		java.util.Map<String, routines.system.TalendDataSource> talendDataSources = new java.util.HashMap<String, routines.system.TalendDataSource>();
		java.util.Map<String, javax.sql.DataSource> dataSources = new java.util.HashMap<String, javax.sql.DataSource>();

		for (java.util.Map.Entry<String, javax.sql.DataSource> entry : BundleUtils
				.getServices(serviceReferences, javax.sql.DataSource.class).entrySet()) {
			dataSources.put(entry.getKey(), entry.getValue());
			talendDataSources.put(entry.getKey(), new routines.system.TalendDataSource(entry.getValue()));
		}

		globalMap.put(KEY_DB_DATASOURCES, talendDataSources);
		globalMap.put(KEY_DB_DATASOURCES_RAW, new java.util.HashMap<String, javax.sql.DataSource>(dataSources));
	}

	private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	private final java.io.PrintStream errorMessagePS = new java.io.PrintStream(new java.io.BufferedOutputStream(baos));

	public String getExceptionStackTrace() {
		if ("failure".equals(this.getStatus())) {
			errorMessagePS.flush();
			return baos.toString();
		}
		return null;
	}

	private Exception exception;

	public Exception getException() {
		if ("failure".equals(this.getStatus())) {
			return this.exception;
		}
		return null;
	}

	private class TalendException extends Exception {

		private static final long serialVersionUID = 1L;

		private java.util.Map<String, Object> globalMap = null;
		private Exception e = null;

		private String currentComponent = null;
		private String cLabel = null;

		private String virtualComponentName = null;

		public void setVirtualComponentName(String virtualComponentName) {
			this.virtualComponentName = virtualComponentName;
		}

		private TalendException(Exception e, String errorComponent, final java.util.Map<String, Object> globalMap) {
			this.currentComponent = errorComponent;
			this.globalMap = globalMap;
			this.e = e;
		}

		private TalendException(Exception e, String errorComponent, String errorComponentLabel,
				final java.util.Map<String, Object> globalMap) {
			this(e, errorComponent, globalMap);
			this.cLabel = errorComponentLabel;
		}

		public Exception getException() {
			return this.e;
		}

		public String getCurrentComponent() {
			return this.currentComponent;
		}

		public String getExceptionCauseMessage(Exception e) {
			Throwable cause = e;
			String message = null;
			int i = 10;
			while (null != cause && 0 < i--) {
				message = cause.getMessage();
				if (null == message) {
					cause = cause.getCause();
				} else {
					break;
				}
			}
			if (null == message) {
				message = e.getClass().getName();
			}
			return message;
		}

		@Override
		public void printStackTrace() {
			if (!(e instanceof TalendException || e instanceof TDieException)) {
				if (virtualComponentName != null && currentComponent.indexOf(virtualComponentName + "_") == 0) {
					globalMap.put(virtualComponentName + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				}
				globalMap.put(currentComponent + "_ERROR_MESSAGE", getExceptionCauseMessage(e));
				System.err.println("Exception in component " + currentComponent + " (" + jobName + ")");
			}
			if (!(e instanceof TDieException)) {
				if (e instanceof TalendException) {
					e.printStackTrace();
				} else {
					e.printStackTrace();
					e.printStackTrace(errorMessagePS);
				}
			}
			if (!(e instanceof TalendException)) {
				encrypt_and_decrypt.this.exception = e;
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(encrypt_and_decrypt.this, new Object[] { e, currentComponent, globalMap });
							break;
						}
					}

					if (!(e instanceof TDieException)) {
						if (enableLogStash) {
							talendJobLog.addJobExceptionMessage(currentComponent, cLabel, null, e);
							talendJobLogProcess(globalMap);
						}
					}
				} catch (Exception e) {
					this.e.printStackTrace();
				}
			}
		}
	}

	public void tFileInputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDataEncrypt_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDataDecrypt_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputDelimited_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class row4Struct implements routines.system.IPersistableRow<row4Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[0];

		public Integer ID;

		public Integer getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return 10;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String Last_Name;

		public String getLast_Name() {
			return this.Last_Name;
		}

		public Boolean Last_NameIsNullable() {
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 30;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

		}

		public String Last_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Last_NameOriginalDbColumnName() {

			return "Last_Name";

		}

		public String First_Name;

		public String getFirst_Name() {
			return this.First_Name;
		}

		public Boolean First_NameIsNullable() {
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 30;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

		}

		public String First_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String First_NameOriginalDbColumnName() {

			return "First_Name";

		}

		public String Middle_Name;

		public String getMiddle_Name() {
			return this.Middle_Name;
		}

		public Boolean Middle_NameIsNullable() {
			return true;
		}

		public Boolean Middle_NameIsKey() {
			return false;
		}

		public Integer Middle_NameLength() {
			return 30;
		}

		public Integer Middle_NamePrecision() {
			return 0;
		}

		public String Middle_NameDefault() {

			return null;

		}

		public String Middle_NameComment() {

			return "";

		}

		public String Middle_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Middle_NameOriginalDbColumnName() {

			return "Middle_Name";

		}

		public String Suffix;

		public String getSuffix() {
			return this.Suffix;
		}

		public Boolean SuffixIsNullable() {
			return true;
		}

		public Boolean SuffixIsKey() {
			return false;
		}

		public Integer SuffixLength() {
			return 10;
		}

		public Integer SuffixPrecision() {
			return 0;
		}

		public String SuffixDefault() {

			return null;

		}

		public String SuffixComment() {

			return "";

		}

		public String SuffixPattern() {

			return "dd-MM-yyyy";

		}

		public String SuffixOriginalDbColumnName() {

			return "Suffix";

		}

		public String SSN;

		public String getSSN() {
			return this.SSN;
		}

		public Boolean SSNIsNullable() {
			return true;
		}

		public Boolean SSNIsKey() {
			return false;
		}

		public Integer SSNLength() {
			return 9;
		}

		public Integer SSNPrecision() {
			return 0;
		}

		public String SSNDefault() {

			return null;

		}

		public String SSNComment() {

			return "";

		}

		public String SSNPattern() {

			return "dd-MM-yyyy";

		}

		public String SSNOriginalDbColumnName() {

			return "SSN";

		}

		public java.util.Date Date_Of_Birth;

		public java.util.Date getDate_Of_Birth() {
			return this.Date_Of_Birth;
		}

		public Boolean Date_Of_BirthIsNullable() {
			return true;
		}

		public Boolean Date_Of_BirthIsKey() {
			return false;
		}

		public Integer Date_Of_BirthLength() {
			return 10;
		}

		public Integer Date_Of_BirthPrecision() {
			return 0;
		}

		public String Date_Of_BirthDefault() {

			return null;

		}

		public String Date_Of_BirthComment() {

			return "";

		}

		public String Date_Of_BirthPattern() {

			return "dd-MM-yyyy";

		}

		public String Date_Of_BirthOriginalDbColumnName() {

			return "Date_Of_Birth";

		}

		public String Address1;

		public String getAddress1() {
			return this.Address1;
		}

		public Boolean Address1IsNullable() {
			return true;
		}

		public Boolean Address1IsKey() {
			return false;
		}

		public Integer Address1Length() {
			return 100;
		}

		public Integer Address1Precision() {
			return 0;
		}

		public String Address1Default() {

			return null;

		}

		public String Address1Comment() {

			return "";

		}

		public String Address1Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address1OriginalDbColumnName() {

			return "Address1";

		}

		public String Address2;

		public String getAddress2() {
			return this.Address2;
		}

		public Boolean Address2IsNullable() {
			return true;
		}

		public Boolean Address2IsKey() {
			return false;
		}

		public Integer Address2Length() {
			return 100;
		}

		public Integer Address2Precision() {
			return 0;
		}

		public String Address2Default() {

			return null;

		}

		public String Address2Comment() {

			return "";

		}

		public String Address2Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address2OriginalDbColumnName() {

			return "Address2";

		}

		public String City;

		public String getCity() {
			return this.City;
		}

		public Boolean CityIsNullable() {
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 50;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

		}

		public String CityPattern() {

			return "dd-MM-yyyy";

		}

		public String CityOriginalDbColumnName() {

			return "City";

		}

		public String State;

		public String getState() {
			return this.State;
		}

		public Boolean StateIsNullable() {
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 10;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "dd-MM-yyyy";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public Integer Zip;

		public Integer getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 10;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

		}

		public String ZipPattern() {

			return "dd-MM-yyyy";

		}

		public String ZipOriginalDbColumnName() {

			return "Zip";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return true;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return 100;
		}

		public Integer EmailPrecision() {
			return 0;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "";

		}

		public String EmailPattern() {

			return "dd-MM-yyyy";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		public String Registration_Date;

		public String getRegistration_Date() {
			return this.Registration_Date;
		}

		public Boolean Registration_DateIsNullable() {
			return true;
		}

		public Boolean Registration_DateIsKey() {
			return false;
		}

		public Integer Registration_DateLength() {
			return 10;
		}

		public Integer Registration_DatePrecision() {
			return 0;
		}

		public String Registration_DateDefault() {

			return null;

		}

		public String Registration_DateComment() {

			return "";

		}

		public String Registration_DatePattern() {

			return "dd-MM-yyyy";

		}

		public String Registration_DateOriginalDbColumnName() {

			return "Registration_Date";

		}

		public String Revenue;

		public String getRevenue() {
			return this.Revenue;
		}

		public Boolean RevenueIsNullable() {
			return true;
		}

		public Boolean RevenueIsKey() {
			return false;
		}

		public Integer RevenueLength() {
			return 10;
		}

		public Integer RevenuePrecision() {
			return 0;
		}

		public String RevenueDefault() {

			return null;

		}

		public String RevenueComment() {

			return "";

		}

		public String RevenuePattern() {

			return "dd-MM-yyyy";

		}

		public String RevenueOriginalDbColumnName() {

			return "Revenue";

		}

		public String Phone;

		public String getPhone() {
			return this.Phone;
		}

		public Boolean PhoneIsNullable() {
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 10;
		}

		public Integer PhonePrecision() {
			return null;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

		}

		public String CreditCard;

		public String getCreditCard() {
			return this.CreditCard;
		}

		public Boolean CreditCardIsNullable() {
			return true;
		}

		public Boolean CreditCardIsKey() {
			return false;
		}

		public Integer CreditCardLength() {
			return 16;
		}

		public Integer CreditCardPrecision() {
			return null;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public Boolean ORIGINAL_MARK;

		public Boolean getORIGINAL_MARK() {
			return this.ORIGINAL_MARK;
		}

		public Boolean ORIGINAL_MARKIsNullable() {
			return true;
		}

		public Boolean ORIGINAL_MARKIsKey() {
			return false;
		}

		public Integer ORIGINAL_MARKLength() {
			return 0;
		}

		public Integer ORIGINAL_MARKPrecision() {
			return 0;
		}

		public String ORIGINAL_MARKDefault() {

			return "";

		}

		public String ORIGINAL_MARKComment() {

			return null;

		}

		public String ORIGINAL_MARKPattern() {

			return null;

		}

		public String ORIGINAL_MARKOriginalDbColumnName() {

			return "ORIGINAL_MARK";

		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private Integer readInteger(org.jboss.marshalling.Unmarshaller dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private void writeInteger(Integer intNum, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (intNum == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeInt(intNum);
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		private java.util.Date readDate(ObjectInputStream dis) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private java.util.Date readDate(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = unmarshaller.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(unmarshaller.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos) throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private void writeDate(java.util.Date date1, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (date1 == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeLong(date1.getTime());
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.ORIGINAL_MARK = null;
					} else {
						this.ORIGINAL_MARK = dis.readBoolean();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.ORIGINAL_MARK = null;
					} else {
						this.ORIGINAL_MARK = dis.readBoolean();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

				// Boolean

				if (this.ORIGINAL_MARK == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeBoolean(this.ORIGINAL_MARK);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

				// Boolean

				if (this.ORIGINAL_MARK == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeBoolean(this.ORIGINAL_MARK);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + String.valueOf(ID));
			sb.append(",Last_Name=" + Last_Name);
			sb.append(",First_Name=" + First_Name);
			sb.append(",Middle_Name=" + Middle_Name);
			sb.append(",Suffix=" + Suffix);
			sb.append(",SSN=" + SSN);
			sb.append(",Date_Of_Birth=" + String.valueOf(Date_Of_Birth));
			sb.append(",Address1=" + Address1);
			sb.append(",Address2=" + Address2);
			sb.append(",City=" + City);
			sb.append(",State=" + State);
			sb.append(",Zip=" + String.valueOf(Zip));
			sb.append(",Email=" + Email);
			sb.append(",Registration_Date=" + Registration_Date);
			sb.append(",Revenue=" + Revenue);
			sb.append(",Phone=" + Phone);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",ORIGINAL_MARK=" + String.valueOf(ORIGINAL_MARK));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (Last_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Last_Name);
			}

			sb.append("|");

			if (First_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(First_Name);
			}

			sb.append("|");

			if (Middle_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Middle_Name);
			}

			sb.append("|");

			if (Suffix == null) {
				sb.append("<null>");
			} else {
				sb.append(Suffix);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (Date_Of_Birth == null) {
				sb.append("<null>");
			} else {
				sb.append(Date_Of_Birth);
			}

			sb.append("|");

			if (Address1 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address1);
			}

			sb.append("|");

			if (Address2 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address2);
			}

			sb.append("|");

			if (City == null) {
				sb.append("<null>");
			} else {
				sb.append(City);
			}

			sb.append("|");

			if (State == null) {
				sb.append("<null>");
			} else {
				sb.append(State);
			}

			sb.append("|");

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			if (Registration_Date == null) {
				sb.append("<null>");
			} else {
				sb.append(Registration_Date);
			}

			sb.append("|");

			if (Revenue == null) {
				sb.append("<null>");
			} else {
				sb.append(Revenue);
			}

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (ORIGINAL_MARK == null) {
				sb.append("<null>");
			} else {
				sb.append(ORIGINAL_MARK);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row4Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row3Struct implements routines.system.IPersistableRow<row3Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[0];

		public Integer ID;

		public Integer getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return 10;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String Last_Name;

		public String getLast_Name() {
			return this.Last_Name;
		}

		public Boolean Last_NameIsNullable() {
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 30;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

		}

		public String Last_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Last_NameOriginalDbColumnName() {

			return "Last_Name";

		}

		public String First_Name;

		public String getFirst_Name() {
			return this.First_Name;
		}

		public Boolean First_NameIsNullable() {
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 30;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

		}

		public String First_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String First_NameOriginalDbColumnName() {

			return "First_Name";

		}

		public String Middle_Name;

		public String getMiddle_Name() {
			return this.Middle_Name;
		}

		public Boolean Middle_NameIsNullable() {
			return true;
		}

		public Boolean Middle_NameIsKey() {
			return false;
		}

		public Integer Middle_NameLength() {
			return 30;
		}

		public Integer Middle_NamePrecision() {
			return 0;
		}

		public String Middle_NameDefault() {

			return null;

		}

		public String Middle_NameComment() {

			return "";

		}

		public String Middle_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Middle_NameOriginalDbColumnName() {

			return "Middle_Name";

		}

		public String Suffix;

		public String getSuffix() {
			return this.Suffix;
		}

		public Boolean SuffixIsNullable() {
			return true;
		}

		public Boolean SuffixIsKey() {
			return false;
		}

		public Integer SuffixLength() {
			return 10;
		}

		public Integer SuffixPrecision() {
			return 0;
		}

		public String SuffixDefault() {

			return null;

		}

		public String SuffixComment() {

			return "";

		}

		public String SuffixPattern() {

			return "dd-MM-yyyy";

		}

		public String SuffixOriginalDbColumnName() {

			return "Suffix";

		}

		public String SSN;

		public String getSSN() {
			return this.SSN;
		}

		public Boolean SSNIsNullable() {
			return true;
		}

		public Boolean SSNIsKey() {
			return false;
		}

		public Integer SSNLength() {
			return 9;
		}

		public Integer SSNPrecision() {
			return 0;
		}

		public String SSNDefault() {

			return null;

		}

		public String SSNComment() {

			return "";

		}

		public String SSNPattern() {

			return "dd-MM-yyyy";

		}

		public String SSNOriginalDbColumnName() {

			return "SSN";

		}

		public java.util.Date Date_Of_Birth;

		public java.util.Date getDate_Of_Birth() {
			return this.Date_Of_Birth;
		}

		public Boolean Date_Of_BirthIsNullable() {
			return true;
		}

		public Boolean Date_Of_BirthIsKey() {
			return false;
		}

		public Integer Date_Of_BirthLength() {
			return 10;
		}

		public Integer Date_Of_BirthPrecision() {
			return 0;
		}

		public String Date_Of_BirthDefault() {

			return null;

		}

		public String Date_Of_BirthComment() {

			return "";

		}

		public String Date_Of_BirthPattern() {

			return "dd-MM-yyyy";

		}

		public String Date_Of_BirthOriginalDbColumnName() {

			return "Date_Of_Birth";

		}

		public String Address1;

		public String getAddress1() {
			return this.Address1;
		}

		public Boolean Address1IsNullable() {
			return true;
		}

		public Boolean Address1IsKey() {
			return false;
		}

		public Integer Address1Length() {
			return 100;
		}

		public Integer Address1Precision() {
			return 0;
		}

		public String Address1Default() {

			return null;

		}

		public String Address1Comment() {

			return "";

		}

		public String Address1Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address1OriginalDbColumnName() {

			return "Address1";

		}

		public String Address2;

		public String getAddress2() {
			return this.Address2;
		}

		public Boolean Address2IsNullable() {
			return true;
		}

		public Boolean Address2IsKey() {
			return false;
		}

		public Integer Address2Length() {
			return 100;
		}

		public Integer Address2Precision() {
			return 0;
		}

		public String Address2Default() {

			return null;

		}

		public String Address2Comment() {

			return "";

		}

		public String Address2Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address2OriginalDbColumnName() {

			return "Address2";

		}

		public String City;

		public String getCity() {
			return this.City;
		}

		public Boolean CityIsNullable() {
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 50;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

		}

		public String CityPattern() {

			return "dd-MM-yyyy";

		}

		public String CityOriginalDbColumnName() {

			return "City";

		}

		public String State;

		public String getState() {
			return this.State;
		}

		public Boolean StateIsNullable() {
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 10;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "dd-MM-yyyy";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public Integer Zip;

		public Integer getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 10;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

		}

		public String ZipPattern() {

			return "dd-MM-yyyy";

		}

		public String ZipOriginalDbColumnName() {

			return "Zip";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return true;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return 100;
		}

		public Integer EmailPrecision() {
			return 0;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "";

		}

		public String EmailPattern() {

			return "dd-MM-yyyy";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		public String Registration_Date;

		public String getRegistration_Date() {
			return this.Registration_Date;
		}

		public Boolean Registration_DateIsNullable() {
			return true;
		}

		public Boolean Registration_DateIsKey() {
			return false;
		}

		public Integer Registration_DateLength() {
			return 10;
		}

		public Integer Registration_DatePrecision() {
			return 0;
		}

		public String Registration_DateDefault() {

			return null;

		}

		public String Registration_DateComment() {

			return "";

		}

		public String Registration_DatePattern() {

			return "dd-MM-yyyy";

		}

		public String Registration_DateOriginalDbColumnName() {

			return "Registration_Date";

		}

		public String Revenue;

		public String getRevenue() {
			return this.Revenue;
		}

		public Boolean RevenueIsNullable() {
			return true;
		}

		public Boolean RevenueIsKey() {
			return false;
		}

		public Integer RevenueLength() {
			return 10;
		}

		public Integer RevenuePrecision() {
			return 0;
		}

		public String RevenueDefault() {

			return null;

		}

		public String RevenueComment() {

			return "";

		}

		public String RevenuePattern() {

			return "dd-MM-yyyy";

		}

		public String RevenueOriginalDbColumnName() {

			return "Revenue";

		}

		public String Phone;

		public String getPhone() {
			return this.Phone;
		}

		public Boolean PhoneIsNullable() {
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 10;
		}

		public Integer PhonePrecision() {
			return null;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

		}

		public String CreditCard;

		public String getCreditCard() {
			return this.CreditCard;
		}

		public Boolean CreditCardIsNullable() {
			return true;
		}

		public Boolean CreditCardIsKey() {
			return false;
		}

		public Integer CreditCardLength() {
			return 16;
		}

		public Integer CreditCardPrecision() {
			return null;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public Boolean ORIGINAL_MARK;

		public Boolean getORIGINAL_MARK() {
			return this.ORIGINAL_MARK;
		}

		public Boolean ORIGINAL_MARKIsNullable() {
			return true;
		}

		public Boolean ORIGINAL_MARKIsKey() {
			return false;
		}

		public Integer ORIGINAL_MARKLength() {
			return 0;
		}

		public Integer ORIGINAL_MARKPrecision() {
			return 0;
		}

		public String ORIGINAL_MARKDefault() {

			return "";

		}

		public String ORIGINAL_MARKComment() {

			return null;

		}

		public String ORIGINAL_MARKPattern() {

			return null;

		}

		public String ORIGINAL_MARKOriginalDbColumnName() {

			return "ORIGINAL_MARK";

		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private Integer readInteger(org.jboss.marshalling.Unmarshaller dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private void writeInteger(Integer intNum, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (intNum == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeInt(intNum);
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		private java.util.Date readDate(ObjectInputStream dis) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private java.util.Date readDate(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = unmarshaller.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(unmarshaller.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos) throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private void writeDate(java.util.Date date1, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (date1 == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeLong(date1.getTime());
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.ORIGINAL_MARK = null;
					} else {
						this.ORIGINAL_MARK = dis.readBoolean();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.ORIGINAL_MARK = null;
					} else {
						this.ORIGINAL_MARK = dis.readBoolean();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

				// Boolean

				if (this.ORIGINAL_MARK == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeBoolean(this.ORIGINAL_MARK);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

				// Boolean

				if (this.ORIGINAL_MARK == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeBoolean(this.ORIGINAL_MARK);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + String.valueOf(ID));
			sb.append(",Last_Name=" + Last_Name);
			sb.append(",First_Name=" + First_Name);
			sb.append(",Middle_Name=" + Middle_Name);
			sb.append(",Suffix=" + Suffix);
			sb.append(",SSN=" + SSN);
			sb.append(",Date_Of_Birth=" + String.valueOf(Date_Of_Birth));
			sb.append(",Address1=" + Address1);
			sb.append(",Address2=" + Address2);
			sb.append(",City=" + City);
			sb.append(",State=" + State);
			sb.append(",Zip=" + String.valueOf(Zip));
			sb.append(",Email=" + Email);
			sb.append(",Registration_Date=" + Registration_Date);
			sb.append(",Revenue=" + Revenue);
			sb.append(",Phone=" + Phone);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",ORIGINAL_MARK=" + String.valueOf(ORIGINAL_MARK));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (Last_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Last_Name);
			}

			sb.append("|");

			if (First_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(First_Name);
			}

			sb.append("|");

			if (Middle_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Middle_Name);
			}

			sb.append("|");

			if (Suffix == null) {
				sb.append("<null>");
			} else {
				sb.append(Suffix);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (Date_Of_Birth == null) {
				sb.append("<null>");
			} else {
				sb.append(Date_Of_Birth);
			}

			sb.append("|");

			if (Address1 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address1);
			}

			sb.append("|");

			if (Address2 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address2);
			}

			sb.append("|");

			if (City == null) {
				sb.append("<null>");
			} else {
				sb.append(City);
			}

			sb.append("|");

			if (State == null) {
				sb.append("<null>");
			} else {
				sb.append(State);
			}

			sb.append("|");

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			if (Registration_Date == null) {
				sb.append("<null>");
			} else {
				sb.append(Registration_Date);
			}

			sb.append("|");

			if (Revenue == null) {
				sb.append("<null>");
			} else {
				sb.append(Revenue);
			}

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (ORIGINAL_MARK == null) {
				sb.append("<null>");
			} else {
				sb.append(ORIGINAL_MARK);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row3Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row2Struct implements routines.system.IPersistableRow<row2Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[0];

		public Integer ID;

		public Integer getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return 10;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String Last_Name;

		public String getLast_Name() {
			return this.Last_Name;
		}

		public Boolean Last_NameIsNullable() {
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 30;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

		}

		public String Last_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Last_NameOriginalDbColumnName() {

			return "Last_Name";

		}

		public String First_Name;

		public String getFirst_Name() {
			return this.First_Name;
		}

		public Boolean First_NameIsNullable() {
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 30;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

		}

		public String First_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String First_NameOriginalDbColumnName() {

			return "First_Name";

		}

		public String Middle_Name;

		public String getMiddle_Name() {
			return this.Middle_Name;
		}

		public Boolean Middle_NameIsNullable() {
			return true;
		}

		public Boolean Middle_NameIsKey() {
			return false;
		}

		public Integer Middle_NameLength() {
			return 30;
		}

		public Integer Middle_NamePrecision() {
			return 0;
		}

		public String Middle_NameDefault() {

			return null;

		}

		public String Middle_NameComment() {

			return "";

		}

		public String Middle_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Middle_NameOriginalDbColumnName() {

			return "Middle_Name";

		}

		public String Suffix;

		public String getSuffix() {
			return this.Suffix;
		}

		public Boolean SuffixIsNullable() {
			return true;
		}

		public Boolean SuffixIsKey() {
			return false;
		}

		public Integer SuffixLength() {
			return 10;
		}

		public Integer SuffixPrecision() {
			return 0;
		}

		public String SuffixDefault() {

			return null;

		}

		public String SuffixComment() {

			return "";

		}

		public String SuffixPattern() {

			return "dd-MM-yyyy";

		}

		public String SuffixOriginalDbColumnName() {

			return "Suffix";

		}

		public String SSN;

		public String getSSN() {
			return this.SSN;
		}

		public Boolean SSNIsNullable() {
			return true;
		}

		public Boolean SSNIsKey() {
			return false;
		}

		public Integer SSNLength() {
			return 9;
		}

		public Integer SSNPrecision() {
			return 0;
		}

		public String SSNDefault() {

			return null;

		}

		public String SSNComment() {

			return "";

		}

		public String SSNPattern() {

			return "dd-MM-yyyy";

		}

		public String SSNOriginalDbColumnName() {

			return "SSN";

		}

		public java.util.Date Date_Of_Birth;

		public java.util.Date getDate_Of_Birth() {
			return this.Date_Of_Birth;
		}

		public Boolean Date_Of_BirthIsNullable() {
			return true;
		}

		public Boolean Date_Of_BirthIsKey() {
			return false;
		}

		public Integer Date_Of_BirthLength() {
			return 10;
		}

		public Integer Date_Of_BirthPrecision() {
			return 0;
		}

		public String Date_Of_BirthDefault() {

			return null;

		}

		public String Date_Of_BirthComment() {

			return "";

		}

		public String Date_Of_BirthPattern() {

			return "dd-MM-yyyy";

		}

		public String Date_Of_BirthOriginalDbColumnName() {

			return "Date_Of_Birth";

		}

		public String Address1;

		public String getAddress1() {
			return this.Address1;
		}

		public Boolean Address1IsNullable() {
			return true;
		}

		public Boolean Address1IsKey() {
			return false;
		}

		public Integer Address1Length() {
			return 100;
		}

		public Integer Address1Precision() {
			return 0;
		}

		public String Address1Default() {

			return null;

		}

		public String Address1Comment() {

			return "";

		}

		public String Address1Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address1OriginalDbColumnName() {

			return "Address1";

		}

		public String Address2;

		public String getAddress2() {
			return this.Address2;
		}

		public Boolean Address2IsNullable() {
			return true;
		}

		public Boolean Address2IsKey() {
			return false;
		}

		public Integer Address2Length() {
			return 100;
		}

		public Integer Address2Precision() {
			return 0;
		}

		public String Address2Default() {

			return null;

		}

		public String Address2Comment() {

			return "";

		}

		public String Address2Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address2OriginalDbColumnName() {

			return "Address2";

		}

		public String City;

		public String getCity() {
			return this.City;
		}

		public Boolean CityIsNullable() {
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 50;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

		}

		public String CityPattern() {

			return "dd-MM-yyyy";

		}

		public String CityOriginalDbColumnName() {

			return "City";

		}

		public String State;

		public String getState() {
			return this.State;
		}

		public Boolean StateIsNullable() {
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 10;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "dd-MM-yyyy";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public Integer Zip;

		public Integer getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 10;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

		}

		public String ZipPattern() {

			return "dd-MM-yyyy";

		}

		public String ZipOriginalDbColumnName() {

			return "Zip";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return true;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return 100;
		}

		public Integer EmailPrecision() {
			return 0;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "";

		}

		public String EmailPattern() {

			return "dd-MM-yyyy";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		public String Registration_Date;

		public String getRegistration_Date() {
			return this.Registration_Date;
		}

		public Boolean Registration_DateIsNullable() {
			return true;
		}

		public Boolean Registration_DateIsKey() {
			return false;
		}

		public Integer Registration_DateLength() {
			return 10;
		}

		public Integer Registration_DatePrecision() {
			return 0;
		}

		public String Registration_DateDefault() {

			return null;

		}

		public String Registration_DateComment() {

			return "";

		}

		public String Registration_DatePattern() {

			return "dd-MM-yyyy";

		}

		public String Registration_DateOriginalDbColumnName() {

			return "Registration_Date";

		}

		public String Revenue;

		public String getRevenue() {
			return this.Revenue;
		}

		public Boolean RevenueIsNullable() {
			return true;
		}

		public Boolean RevenueIsKey() {
			return false;
		}

		public Integer RevenueLength() {
			return 10;
		}

		public Integer RevenuePrecision() {
			return 0;
		}

		public String RevenueDefault() {

			return null;

		}

		public String RevenueComment() {

			return "";

		}

		public String RevenuePattern() {

			return "dd-MM-yyyy";

		}

		public String RevenueOriginalDbColumnName() {

			return "Revenue";

		}

		public String Phone;

		public String getPhone() {
			return this.Phone;
		}

		public Boolean PhoneIsNullable() {
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 10;
		}

		public Integer PhonePrecision() {
			return null;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

		}

		public String CreditCard;

		public String getCreditCard() {
			return this.CreditCard;
		}

		public Boolean CreditCardIsNullable() {
			return true;
		}

		public Boolean CreditCardIsKey() {
			return false;
		}

		public Integer CreditCardLength() {
			return 16;
		}

		public Integer CreditCardPrecision() {
			return null;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public Boolean ORIGINAL_MARK;

		public Boolean getORIGINAL_MARK() {
			return this.ORIGINAL_MARK;
		}

		public Boolean ORIGINAL_MARKIsNullable() {
			return true;
		}

		public Boolean ORIGINAL_MARKIsKey() {
			return false;
		}

		public Integer ORIGINAL_MARKLength() {
			return 0;
		}

		public Integer ORIGINAL_MARKPrecision() {
			return 0;
		}

		public String ORIGINAL_MARKDefault() {

			return "";

		}

		public String ORIGINAL_MARKComment() {

			return null;

		}

		public String ORIGINAL_MARKPattern() {

			return null;

		}

		public String ORIGINAL_MARKOriginalDbColumnName() {

			return "ORIGINAL_MARK";

		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private Integer readInteger(org.jboss.marshalling.Unmarshaller dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private void writeInteger(Integer intNum, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (intNum == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeInt(intNum);
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		private java.util.Date readDate(ObjectInputStream dis) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private java.util.Date readDate(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = unmarshaller.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(unmarshaller.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos) throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private void writeDate(java.util.Date date1, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (date1 == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeLong(date1.getTime());
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.ORIGINAL_MARK = null;
					} else {
						this.ORIGINAL_MARK = dis.readBoolean();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.ORIGINAL_MARK = null;
					} else {
						this.ORIGINAL_MARK = dis.readBoolean();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

				// Boolean

				if (this.ORIGINAL_MARK == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeBoolean(this.ORIGINAL_MARK);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

				// Boolean

				if (this.ORIGINAL_MARK == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeBoolean(this.ORIGINAL_MARK);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + String.valueOf(ID));
			sb.append(",Last_Name=" + Last_Name);
			sb.append(",First_Name=" + First_Name);
			sb.append(",Middle_Name=" + Middle_Name);
			sb.append(",Suffix=" + Suffix);
			sb.append(",SSN=" + SSN);
			sb.append(",Date_Of_Birth=" + String.valueOf(Date_Of_Birth));
			sb.append(",Address1=" + Address1);
			sb.append(",Address2=" + Address2);
			sb.append(",City=" + City);
			sb.append(",State=" + State);
			sb.append(",Zip=" + String.valueOf(Zip));
			sb.append(",Email=" + Email);
			sb.append(",Registration_Date=" + Registration_Date);
			sb.append(",Revenue=" + Revenue);
			sb.append(",Phone=" + Phone);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",ORIGINAL_MARK=" + String.valueOf(ORIGINAL_MARK));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (Last_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Last_Name);
			}

			sb.append("|");

			if (First_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(First_Name);
			}

			sb.append("|");

			if (Middle_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Middle_Name);
			}

			sb.append("|");

			if (Suffix == null) {
				sb.append("<null>");
			} else {
				sb.append(Suffix);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (Date_Of_Birth == null) {
				sb.append("<null>");
			} else {
				sb.append(Date_Of_Birth);
			}

			sb.append("|");

			if (Address1 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address1);
			}

			sb.append("|");

			if (Address2 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address2);
			}

			sb.append("|");

			if (City == null) {
				sb.append("<null>");
			} else {
				sb.append(City);
			}

			sb.append("|");

			if (State == null) {
				sb.append("<null>");
			} else {
				sb.append(State);
			}

			sb.append("|");

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			if (Registration_Date == null) {
				sb.append("<null>");
			} else {
				sb.append(Registration_Date);
			}

			sb.append("|");

			if (Revenue == null) {
				sb.append("<null>");
			} else {
				sb.append(Revenue);
			}

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (ORIGINAL_MARK == null) {
				sb.append("<null>");
			} else {
				sb.append(ORIGINAL_MARK);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row2Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[0];

		public Integer ID;

		public Integer getID() {
			return this.ID;
		}

		public Boolean IDIsNullable() {
			return true;
		}

		public Boolean IDIsKey() {
			return false;
		}

		public Integer IDLength() {
			return 10;
		}

		public Integer IDPrecision() {
			return 0;
		}

		public String IDDefault() {

			return null;

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "dd-MM-yyyy";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

		public String Last_Name;

		public String getLast_Name() {
			return this.Last_Name;
		}

		public Boolean Last_NameIsNullable() {
			return true;
		}

		public Boolean Last_NameIsKey() {
			return false;
		}

		public Integer Last_NameLength() {
			return 30;
		}

		public Integer Last_NamePrecision() {
			return 0;
		}

		public String Last_NameDefault() {

			return null;

		}

		public String Last_NameComment() {

			return "";

		}

		public String Last_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Last_NameOriginalDbColumnName() {

			return "Last_Name";

		}

		public String First_Name;

		public String getFirst_Name() {
			return this.First_Name;
		}

		public Boolean First_NameIsNullable() {
			return true;
		}

		public Boolean First_NameIsKey() {
			return false;
		}

		public Integer First_NameLength() {
			return 30;
		}

		public Integer First_NamePrecision() {
			return 0;
		}

		public String First_NameDefault() {

			return null;

		}

		public String First_NameComment() {

			return "";

		}

		public String First_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String First_NameOriginalDbColumnName() {

			return "First_Name";

		}

		public String Middle_Name;

		public String getMiddle_Name() {
			return this.Middle_Name;
		}

		public Boolean Middle_NameIsNullable() {
			return true;
		}

		public Boolean Middle_NameIsKey() {
			return false;
		}

		public Integer Middle_NameLength() {
			return 30;
		}

		public Integer Middle_NamePrecision() {
			return 0;
		}

		public String Middle_NameDefault() {

			return null;

		}

		public String Middle_NameComment() {

			return "";

		}

		public String Middle_NamePattern() {

			return "dd-MM-yyyy";

		}

		public String Middle_NameOriginalDbColumnName() {

			return "Middle_Name";

		}

		public String Suffix;

		public String getSuffix() {
			return this.Suffix;
		}

		public Boolean SuffixIsNullable() {
			return true;
		}

		public Boolean SuffixIsKey() {
			return false;
		}

		public Integer SuffixLength() {
			return 10;
		}

		public Integer SuffixPrecision() {
			return 0;
		}

		public String SuffixDefault() {

			return null;

		}

		public String SuffixComment() {

			return "";

		}

		public String SuffixPattern() {

			return "dd-MM-yyyy";

		}

		public String SuffixOriginalDbColumnName() {

			return "Suffix";

		}

		public String SSN;

		public String getSSN() {
			return this.SSN;
		}

		public Boolean SSNIsNullable() {
			return true;
		}

		public Boolean SSNIsKey() {
			return false;
		}

		public Integer SSNLength() {
			return 9;
		}

		public Integer SSNPrecision() {
			return 0;
		}

		public String SSNDefault() {

			return null;

		}

		public String SSNComment() {

			return "";

		}

		public String SSNPattern() {

			return "dd-MM-yyyy";

		}

		public String SSNOriginalDbColumnName() {

			return "SSN";

		}

		public java.util.Date Date_Of_Birth;

		public java.util.Date getDate_Of_Birth() {
			return this.Date_Of_Birth;
		}

		public Boolean Date_Of_BirthIsNullable() {
			return true;
		}

		public Boolean Date_Of_BirthIsKey() {
			return false;
		}

		public Integer Date_Of_BirthLength() {
			return 10;
		}

		public Integer Date_Of_BirthPrecision() {
			return 0;
		}

		public String Date_Of_BirthDefault() {

			return null;

		}

		public String Date_Of_BirthComment() {

			return "";

		}

		public String Date_Of_BirthPattern() {

			return "dd-MM-yyyy";

		}

		public String Date_Of_BirthOriginalDbColumnName() {

			return "Date_Of_Birth";

		}

		public String Address1;

		public String getAddress1() {
			return this.Address1;
		}

		public Boolean Address1IsNullable() {
			return true;
		}

		public Boolean Address1IsKey() {
			return false;
		}

		public Integer Address1Length() {
			return 100;
		}

		public Integer Address1Precision() {
			return 0;
		}

		public String Address1Default() {

			return null;

		}

		public String Address1Comment() {

			return "";

		}

		public String Address1Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address1OriginalDbColumnName() {

			return "Address1";

		}

		public String Address2;

		public String getAddress2() {
			return this.Address2;
		}

		public Boolean Address2IsNullable() {
			return true;
		}

		public Boolean Address2IsKey() {
			return false;
		}

		public Integer Address2Length() {
			return 100;
		}

		public Integer Address2Precision() {
			return 0;
		}

		public String Address2Default() {

			return null;

		}

		public String Address2Comment() {

			return "";

		}

		public String Address2Pattern() {

			return "dd-MM-yyyy";

		}

		public String Address2OriginalDbColumnName() {

			return "Address2";

		}

		public String City;

		public String getCity() {
			return this.City;
		}

		public Boolean CityIsNullable() {
			return true;
		}

		public Boolean CityIsKey() {
			return false;
		}

		public Integer CityLength() {
			return 50;
		}

		public Integer CityPrecision() {
			return 0;
		}

		public String CityDefault() {

			return null;

		}

		public String CityComment() {

			return "";

		}

		public String CityPattern() {

			return "dd-MM-yyyy";

		}

		public String CityOriginalDbColumnName() {

			return "City";

		}

		public String State;

		public String getState() {
			return this.State;
		}

		public Boolean StateIsNullable() {
			return true;
		}

		public Boolean StateIsKey() {
			return false;
		}

		public Integer StateLength() {
			return 10;
		}

		public Integer StatePrecision() {
			return 0;
		}

		public String StateDefault() {

			return null;

		}

		public String StateComment() {

			return "";

		}

		public String StatePattern() {

			return "dd-MM-yyyy";

		}

		public String StateOriginalDbColumnName() {

			return "State";

		}

		public Integer Zip;

		public Integer getZip() {
			return this.Zip;
		}

		public Boolean ZipIsNullable() {
			return true;
		}

		public Boolean ZipIsKey() {
			return false;
		}

		public Integer ZipLength() {
			return 10;
		}

		public Integer ZipPrecision() {
			return 0;
		}

		public String ZipDefault() {

			return null;

		}

		public String ZipComment() {

			return "";

		}

		public String ZipPattern() {

			return "dd-MM-yyyy";

		}

		public String ZipOriginalDbColumnName() {

			return "Zip";

		}

		public String Email;

		public String getEmail() {
			return this.Email;
		}

		public Boolean EmailIsNullable() {
			return true;
		}

		public Boolean EmailIsKey() {
			return false;
		}

		public Integer EmailLength() {
			return 100;
		}

		public Integer EmailPrecision() {
			return 0;
		}

		public String EmailDefault() {

			return null;

		}

		public String EmailComment() {

			return "";

		}

		public String EmailPattern() {

			return "dd-MM-yyyy";

		}

		public String EmailOriginalDbColumnName() {

			return "Email";

		}

		public String Registration_Date;

		public String getRegistration_Date() {
			return this.Registration_Date;
		}

		public Boolean Registration_DateIsNullable() {
			return true;
		}

		public Boolean Registration_DateIsKey() {
			return false;
		}

		public Integer Registration_DateLength() {
			return 10;
		}

		public Integer Registration_DatePrecision() {
			return 0;
		}

		public String Registration_DateDefault() {

			return null;

		}

		public String Registration_DateComment() {

			return "";

		}

		public String Registration_DatePattern() {

			return "dd-MM-yyyy";

		}

		public String Registration_DateOriginalDbColumnName() {

			return "Registration_Date";

		}

		public String Revenue;

		public String getRevenue() {
			return this.Revenue;
		}

		public Boolean RevenueIsNullable() {
			return true;
		}

		public Boolean RevenueIsKey() {
			return false;
		}

		public Integer RevenueLength() {
			return 10;
		}

		public Integer RevenuePrecision() {
			return 0;
		}

		public String RevenueDefault() {

			return null;

		}

		public String RevenueComment() {

			return "";

		}

		public String RevenuePattern() {

			return "dd-MM-yyyy";

		}

		public String RevenueOriginalDbColumnName() {

			return "Revenue";

		}

		public String Phone;

		public String getPhone() {
			return this.Phone;
		}

		public Boolean PhoneIsNullable() {
			return true;
		}

		public Boolean PhoneIsKey() {
			return false;
		}

		public Integer PhoneLength() {
			return 10;
		}

		public Integer PhonePrecision() {
			return null;
		}

		public String PhoneDefault() {

			return null;

		}

		public String PhoneComment() {

			return "";

		}

		public String PhonePattern() {

			return "";

		}

		public String PhoneOriginalDbColumnName() {

			return "Phone";

		}

		public String CreditCard;

		public String getCreditCard() {
			return this.CreditCard;
		}

		public Boolean CreditCardIsNullable() {
			return true;
		}

		public Boolean CreditCardIsKey() {
			return false;
		}

		public Integer CreditCardLength() {
			return 16;
		}

		public Integer CreditCardPrecision() {
			return null;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		private Integer readInteger(ObjectInputStream dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private Integer readInteger(org.jboss.marshalling.Unmarshaller dis) throws IOException {
			Integer intReturn;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				intReturn = null;
			} else {
				intReturn = dis.readInt();
			}
			return intReturn;
		}

		private void writeInteger(Integer intNum, ObjectOutputStream dos) throws IOException {
			if (intNum == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeInt(intNum);
			}
		}

		private void writeInteger(Integer intNum, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (intNum == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeInt(intNum);
			}
		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private String readString(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			String strReturn = null;
			int length = 0;
			length = unmarshaller.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_encrypt_and_decrypt.length == 0) {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_encrypt_and_decrypt = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_encrypt_and_decrypt, 0, length, utf8Charset);
			}
			return strReturn;
		}

		private void writeString(String str, ObjectOutputStream dos) throws IOException {
			if (str == null) {
				dos.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				dos.writeInt(byteArray.length);
				dos.write(byteArray);
			}
		}

		private void writeString(String str, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (str == null) {
				marshaller.writeInt(-1);
			} else {
				byte[] byteArray = str.getBytes(utf8Charset);
				marshaller.writeInt(byteArray.length);
				marshaller.write(byteArray);
			}
		}

		private java.util.Date readDate(ObjectInputStream dis) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = dis.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(dis.readLong());
			}
			return dateReturn;
		}

		private java.util.Date readDate(org.jboss.marshalling.Unmarshaller unmarshaller) throws IOException {
			java.util.Date dateReturn = null;
			int length = 0;
			length = unmarshaller.readByte();
			if (length == -1) {
				dateReturn = null;
			} else {
				dateReturn = new Date(unmarshaller.readLong());
			}
			return dateReturn;
		}

		private void writeDate(java.util.Date date1, ObjectOutputStream dos) throws IOException {
			if (date1 == null) {
				dos.writeByte(-1);
			} else {
				dos.writeByte(0);
				dos.writeLong(date1.getTime());
			}
		}

		private void writeDate(java.util.Date date1, org.jboss.marshalling.Marshaller marshaller) throws IOException {
			if (date1 == null) {
				marshaller.writeByte(-1);
			} else {
				marshaller.writeByte(0);
				marshaller.writeLong(date1.getTime());
			}
		}

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_encrypt_and_decrypt) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.Last_Name = readString(dis);

					this.First_Name = readString(dis);

					this.Middle_Name = readString(dis);

					this.Suffix = readString(dis);

					this.SSN = readString(dis);

					this.Date_Of_Birth = readDate(dis);

					this.Address1 = readString(dis);

					this.Address2 = readString(dis);

					this.City = readString(dis);

					this.State = readString(dis);

					this.Zip = readInteger(dis);

					this.Email = readString(dis);

					this.Registration_Date = readString(dis);

					this.Revenue = readString(dis);

					this.Phone = readString(dis);

					this.CreditCard = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.Last_Name, dos);

				// String

				writeString(this.First_Name, dos);

				// String

				writeString(this.Middle_Name, dos);

				// String

				writeString(this.Suffix, dos);

				// String

				writeString(this.SSN, dos);

				// java.util.Date

				writeDate(this.Date_Of_Birth, dos);

				// String

				writeString(this.Address1, dos);

				// String

				writeString(this.Address2, dos);

				// String

				writeString(this.City, dos);

				// String

				writeString(this.State, dos);

				// Integer

				writeInteger(this.Zip, dos);

				// String

				writeString(this.Email, dos);

				// String

				writeString(this.Registration_Date, dos);

				// String

				writeString(this.Revenue, dos);

				// String

				writeString(this.Phone, dos);

				// String

				writeString(this.CreditCard, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + String.valueOf(ID));
			sb.append(",Last_Name=" + Last_Name);
			sb.append(",First_Name=" + First_Name);
			sb.append(",Middle_Name=" + Middle_Name);
			sb.append(",Suffix=" + Suffix);
			sb.append(",SSN=" + SSN);
			sb.append(",Date_Of_Birth=" + String.valueOf(Date_Of_Birth));
			sb.append(",Address1=" + Address1);
			sb.append(",Address2=" + Address2);
			sb.append(",City=" + City);
			sb.append(",State=" + State);
			sb.append(",Zip=" + String.valueOf(Zip));
			sb.append(",Email=" + Email);
			sb.append(",Registration_Date=" + Registration_Date);
			sb.append(",Revenue=" + Revenue);
			sb.append(",Phone=" + Phone);
			sb.append(",CreditCard=" + CreditCard);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (ID == null) {
				sb.append("<null>");
			} else {
				sb.append(ID);
			}

			sb.append("|");

			if (Last_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Last_Name);
			}

			sb.append("|");

			if (First_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(First_Name);
			}

			sb.append("|");

			if (Middle_Name == null) {
				sb.append("<null>");
			} else {
				sb.append(Middle_Name);
			}

			sb.append("|");

			if (Suffix == null) {
				sb.append("<null>");
			} else {
				sb.append(Suffix);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (Date_Of_Birth == null) {
				sb.append("<null>");
			} else {
				sb.append(Date_Of_Birth);
			}

			sb.append("|");

			if (Address1 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address1);
			}

			sb.append("|");

			if (Address2 == null) {
				sb.append("<null>");
			} else {
				sb.append(Address2);
			}

			sb.append("|");

			if (City == null) {
				sb.append("<null>");
			} else {
				sb.append(City);
			}

			sb.append("|");

			if (State == null) {
				sb.append("<null>");
			} else {
				sb.append(State);
			}

			sb.append("|");

			if (Zip == null) {
				sb.append("<null>");
			} else {
				sb.append(Zip);
			}

			sb.append("|");

			if (Email == null) {
				sb.append("<null>");
			} else {
				sb.append(Email);
			}

			sb.append("|");

			if (Registration_Date == null) {
				sb.append("<null>");
			} else {
				sb.append(Registration_Date);
			}

			sb.append("|");

			if (Revenue == null) {
				sb.append("<null>");
			} else {
				sb.append(Revenue);
			}

			sb.append("|");

			if (Phone == null) {
				sb.append("<null>");
			} else {
				sb.append(Phone);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row1Struct other) {

			int returnValue = -1;

			return returnValue;
		}

		private int checkNullsAndCompare(Object object1, Object object2) {
			int returnValue = 0;
			if (object1 instanceof Comparable && object2 instanceof Comparable) {
				returnValue = ((Comparable) object1).compareTo(object2);
			} else if (object1 != null && object2 != null) {
				returnValue = compareStrings(object1.toString(), object2.toString());
			} else if (object1 == null && object2 != null) {
				returnValue = 1;
			} else if (object1 != null && object2 == null) {
				returnValue = -1;
			} else {
				returnValue = 0;
			}

			return returnValue;
		}

		private int compareStrings(String string1, String string2) {
			return string1.compareTo(string2);
		}

	}

	public void tFileInputDelimited_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tFileInputDelimited_1", "T35b3R_");

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				row1Struct row1 = new row1Struct();
				row2Struct row2 = new row2Struct();
				row2Struct row3 = row2;
				row4Struct row4 = new row4Struct();

				/**
				 * [tLogRow_2 begin ] start
				 */

				sh("tLogRow_2");

				s(currentComponent = "tLogRow_2");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row4");

				int tos_count_tLogRow_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tLogRow_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tLogRow_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tLogRow_2 = new StringBuilder();
							log4jParamters_tLogRow_2.append("Parameters:");
							log4jParamters_tLogRow_2.append("BASIC_MODE" + " = " + "false");
							log4jParamters_tLogRow_2.append(" | ");
							log4jParamters_tLogRow_2.append("TABLE_PRINT" + " = " + "true");
							log4jParamters_tLogRow_2.append(" | ");
							log4jParamters_tLogRow_2.append("VERTICAL" + " = " + "false");
							log4jParamters_tLogRow_2.append(" | ");
							log4jParamters_tLogRow_2.append("PRINT_CONTENT_WITH_LOG4J" + " = " + "true");
							log4jParamters_tLogRow_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tLogRow_2 - " + (log4jParamters_tLogRow_2));
						}
					}
					new BytesLimit65535_tLogRow_2().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tLogRow_2", "tLogRow_2", "tLogRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				///////////////////////

				class Util_tLogRow_2 {

					String[] des_top = { ".", ".", "-", "+" };

					String[] des_head = { "|=", "=|", "-", "+" };

					String[] des_bottom = { "'", "'", "-", "+" };

					String name = "";

					java.util.List<String[]> list = new java.util.ArrayList<String[]>();

					int[] colLengths = new int[18];

					public void addRow(String[] row) {

						for (int i = 0; i < 18; i++) {
							if (row[i] != null) {
								colLengths[i] = Math.max(colLengths[i], row[i].length());
							}
						}
						list.add(row);
					}

					public void setTableName(String name) {

						this.name = name;
					}

					public StringBuilder format() {

						StringBuilder sb = new StringBuilder();

						sb.append(print(des_top));

						int totals = 0;
						for (int i = 0; i < colLengths.length; i++) {
							totals = totals + colLengths[i];
						}

						// name
						sb.append("|");
						int k = 0;
						for (k = 0; k < (totals + 17 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 17 - name.length() - k; i++) {
							sb.append(' ');
						}
						sb.append("|\n");

						// head and rows
						sb.append(print(des_head));
						for (int i = 0; i < list.size(); i++) {

							String[] row = list.get(i);

							java.util.Formatter formatter = new java.util.Formatter(new StringBuilder());

							StringBuilder sbformat = new StringBuilder();
							sbformat.append("|%1$-");
							sbformat.append(colLengths[0]);
							sbformat.append("s");

							sbformat.append("|%2$-");
							sbformat.append(colLengths[1]);
							sbformat.append("s");

							sbformat.append("|%3$-");
							sbformat.append(colLengths[2]);
							sbformat.append("s");

							sbformat.append("|%4$-");
							sbformat.append(colLengths[3]);
							sbformat.append("s");

							sbformat.append("|%5$-");
							sbformat.append(colLengths[4]);
							sbformat.append("s");

							sbformat.append("|%6$-");
							sbformat.append(colLengths[5]);
							sbformat.append("s");

							sbformat.append("|%7$-");
							sbformat.append(colLengths[6]);
							sbformat.append("s");

							sbformat.append("|%8$-");
							sbformat.append(colLengths[7]);
							sbformat.append("s");

							sbformat.append("|%9$-");
							sbformat.append(colLengths[8]);
							sbformat.append("s");

							sbformat.append("|%10$-");
							sbformat.append(colLengths[9]);
							sbformat.append("s");

							sbformat.append("|%11$-");
							sbformat.append(colLengths[10]);
							sbformat.append("s");

							sbformat.append("|%12$-");
							sbformat.append(colLengths[11]);
							sbformat.append("s");

							sbformat.append("|%13$-");
							sbformat.append(colLengths[12]);
							sbformat.append("s");

							sbformat.append("|%14$-");
							sbformat.append(colLengths[13]);
							sbformat.append("s");

							sbformat.append("|%15$-");
							sbformat.append(colLengths[14]);
							sbformat.append("s");

							sbformat.append("|%16$-");
							sbformat.append(colLengths[15]);
							sbformat.append("s");

							sbformat.append("|%17$-");
							sbformat.append(colLengths[16]);
							sbformat.append("s");

							sbformat.append("|%18$-");
							sbformat.append(colLengths[17]);
							sbformat.append("s");

							sbformat.append("|\n");

							formatter.format(sbformat.toString(), (Object[]) row);

							sb.append(formatter.toString());
							if (i == 0)
								sb.append(print(des_head)); // print the head
						}

						// end
						sb.append(print(des_bottom));
						return sb;
					}

					private StringBuilder print(String[] fillChars) {
						StringBuilder sb = new StringBuilder();
						// first column
						sb.append(fillChars[0]);
						for (int i = 0; i < colLengths[0] - fillChars[0].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						for (int i = 0; i < colLengths[1] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[2] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[3] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[4] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[5] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[6] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[7] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[8] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[9] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[10] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[11] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[12] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[13] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[14] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[15] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[16] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						// last column
						for (int i = 0; i < colLengths[17] - fillChars[1].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[1]);
						sb.append("\n");
						return sb;
					}

					public boolean isTableEmpty() {
						if (list.size() > 1)
							return false;
						return true;
					}
				}
				Util_tLogRow_2 util_tLogRow_2 = new Util_tLogRow_2();
				util_tLogRow_2.setTableName("tLogRow_2");
				util_tLogRow_2.addRow(new String[] { "ID", "Last_Name", "First_Name", "Middle_Name", "Suffix", "SSN",
						"Date_Of_Birth", "Address1", "Address2", "City", "State", "Zip", "Email", "Registration_Date",
						"Revenue", "Phone", "CreditCard", "ORIGINAL_MARK", });
				StringBuilder strBuffer_tLogRow_2 = null;
				int nb_line_tLogRow_2 = 0;
///////////////////////    			

				/**
				 * [tLogRow_2 begin ] stop
				 */

				/**
				 * [tDataDecrypt_1 begin ] start
				 */

				sh("tDataDecrypt_1");

				s(currentComponent = "tDataDecrypt_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row3");

				int tos_count_tDataDecrypt_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDataDecrypt_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDataDecrypt_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDataDecrypt_1 = new StringBuilder();
							log4jParamters_tDataDecrypt_1.append("Parameters:");
							log4jParamters_tDataDecrypt_1.append("SECRET_TYPE" + " = " + "CRYPTO_FILE");
							log4jParamters_tDataDecrypt_1.append(" | ");
							log4jParamters_tDataDecrypt_1.append("PASSWORD_M1" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:9lhVgD9bcFrvIB9P9sXlZ+wRh8TCH36dxU3m8ITszCCH9XS2")
									.substring(0, 4) + "...");
							log4jParamters_tDataDecrypt_1.append(" | ");
							log4jParamters_tDataDecrypt_1.append("CRYPTO_FILE_PATH" + " = " + "context.CyrptoFilePath");
							log4jParamters_tDataDecrypt_1.append(" | ");
							log4jParamters_tDataDecrypt_1.append("TABLE_CONFIG" + " = " + "[{COLUMNS_DECRYPT="
									+ ("false") + ", SCHEMA_COLUMN=" + ("ID") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Last_Name") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("First_Name") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Middle_Name") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Suffix") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("SSN") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Date_Of_Birth") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Address1") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Address2") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("City") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("State") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Zip") + "}, {COLUMNS_DECRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Email") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Registration_Date") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Revenue") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Phone") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("CreditCard") + "}, {COLUMNS_DECRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("ORIGINAL_MARK") + "}]");
							log4jParamters_tDataDecrypt_1.append(" | ");
							log4jParamters_tDataDecrypt_1.append("DIE_ON_ERROR" + " = " + "true");
							log4jParamters_tDataDecrypt_1.append(" | ");
							log4jParamters_tDataDecrypt_1.append("ALGO_VERSION" + " = " + "1");
							log4jParamters_tDataDecrypt_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDataDecrypt_1 - " + (log4jParamters_tDataDecrypt_1));
						}
					}
					new BytesLimit65535_tDataDecrypt_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tDataDecrypt_1", "tDataDecrypt_1", "tDataDecrypt");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				class tDataEncryptUtils_tDataDecrypt_1 {
					public void typeMismatch(String message) {
						throw new RuntimeException(message);
					}
				}

				tDataEncryptUtils_tDataDecrypt_1 utils_tDataDecrypt_1 = new tDataEncryptUtils_tDataDecrypt_1();
				// generate file
				if (true && (context.CyrptoFilePath == null || "".equals(context.CyrptoFilePath.trim()))) {
					throw new RuntimeException(
							"[ERROR]: Please make sure that the cryptographic file path has been configured.");
				}

				int nb_line_tDataDecrypt_1 = 0;

				org.talend.dataquality.encryption.EncryptionService encryptionService_tDataDecrypt_1;
				encryptionService_tDataDecrypt_1 = new org.talend.dataquality.encryption.EncryptionService(
						routines.system.PasswordEncryptUtil.decryptPassword(
								"enc:routine.encryption.key.v1:6mEk2z6nA+9GcnmVCdDGC3XBc89RnUIws+zy5aSfCYnpTvDo"),
						context.CyrptoFilePath, true, 1);

				/**
				 * [tDataDecrypt_1 begin ] stop
				 */

				/**
				 * [tLogRow_1 begin ] start
				 */

				sh("tLogRow_1");

				s(currentComponent = "tLogRow_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row2");

				int tos_count_tLogRow_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tLogRow_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tLogRow_1 = new StringBuilder();
							log4jParamters_tLogRow_1.append("Parameters:");
							log4jParamters_tLogRow_1.append("BASIC_MODE" + " = " + "false");
							log4jParamters_tLogRow_1.append(" | ");
							log4jParamters_tLogRow_1.append("TABLE_PRINT" + " = " + "true");
							log4jParamters_tLogRow_1.append(" | ");
							log4jParamters_tLogRow_1.append("VERTICAL" + " = " + "false");
							log4jParamters_tLogRow_1.append(" | ");
							log4jParamters_tLogRow_1.append("PRINT_CONTENT_WITH_LOG4J" + " = " + "true");
							log4jParamters_tLogRow_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tLogRow_1 - " + (log4jParamters_tLogRow_1));
						}
					}
					new BytesLimit65535_tLogRow_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tLogRow_1", "tLogRow_1", "tLogRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				///////////////////////

				class Util_tLogRow_1 {

					String[] des_top = { ".", ".", "-", "+" };

					String[] des_head = { "|=", "=|", "-", "+" };

					String[] des_bottom = { "'", "'", "-", "+" };

					String name = "";

					java.util.List<String[]> list = new java.util.ArrayList<String[]>();

					int[] colLengths = new int[18];

					public void addRow(String[] row) {

						for (int i = 0; i < 18; i++) {
							if (row[i] != null) {
								colLengths[i] = Math.max(colLengths[i], row[i].length());
							}
						}
						list.add(row);
					}

					public void setTableName(String name) {

						this.name = name;
					}

					public StringBuilder format() {

						StringBuilder sb = new StringBuilder();

						sb.append(print(des_top));

						int totals = 0;
						for (int i = 0; i < colLengths.length; i++) {
							totals = totals + colLengths[i];
						}

						// name
						sb.append("|");
						int k = 0;
						for (k = 0; k < (totals + 17 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 17 - name.length() - k; i++) {
							sb.append(' ');
						}
						sb.append("|\n");

						// head and rows
						sb.append(print(des_head));
						for (int i = 0; i < list.size(); i++) {

							String[] row = list.get(i);

							java.util.Formatter formatter = new java.util.Formatter(new StringBuilder());

							StringBuilder sbformat = new StringBuilder();
							sbformat.append("|%1$-");
							sbformat.append(colLengths[0]);
							sbformat.append("s");

							sbformat.append("|%2$-");
							sbformat.append(colLengths[1]);
							sbformat.append("s");

							sbformat.append("|%3$-");
							sbformat.append(colLengths[2]);
							sbformat.append("s");

							sbformat.append("|%4$-");
							sbformat.append(colLengths[3]);
							sbformat.append("s");

							sbformat.append("|%5$-");
							sbformat.append(colLengths[4]);
							sbformat.append("s");

							sbformat.append("|%6$-");
							sbformat.append(colLengths[5]);
							sbformat.append("s");

							sbformat.append("|%7$-");
							sbformat.append(colLengths[6]);
							sbformat.append("s");

							sbformat.append("|%8$-");
							sbformat.append(colLengths[7]);
							sbformat.append("s");

							sbformat.append("|%9$-");
							sbformat.append(colLengths[8]);
							sbformat.append("s");

							sbformat.append("|%10$-");
							sbformat.append(colLengths[9]);
							sbformat.append("s");

							sbformat.append("|%11$-");
							sbformat.append(colLengths[10]);
							sbformat.append("s");

							sbformat.append("|%12$-");
							sbformat.append(colLengths[11]);
							sbformat.append("s");

							sbformat.append("|%13$-");
							sbformat.append(colLengths[12]);
							sbformat.append("s");

							sbformat.append("|%14$-");
							sbformat.append(colLengths[13]);
							sbformat.append("s");

							sbformat.append("|%15$-");
							sbformat.append(colLengths[14]);
							sbformat.append("s");

							sbformat.append("|%16$-");
							sbformat.append(colLengths[15]);
							sbformat.append("s");

							sbformat.append("|%17$-");
							sbformat.append(colLengths[16]);
							sbformat.append("s");

							sbformat.append("|%18$-");
							sbformat.append(colLengths[17]);
							sbformat.append("s");

							sbformat.append("|\n");

							formatter.format(sbformat.toString(), (Object[]) row);

							sb.append(formatter.toString());
							if (i == 0)
								sb.append(print(des_head)); // print the head
						}

						// end
						sb.append(print(des_bottom));
						return sb;
					}

					private StringBuilder print(String[] fillChars) {
						StringBuilder sb = new StringBuilder();
						// first column
						sb.append(fillChars[0]);
						for (int i = 0; i < colLengths[0] - fillChars[0].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						for (int i = 0; i < colLengths[1] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[2] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[3] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[4] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[5] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[6] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[7] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[8] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[9] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[10] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[11] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[12] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[13] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[14] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[15] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);
						for (int i = 0; i < colLengths[16] - fillChars[3].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[3]);

						// last column
						for (int i = 0; i < colLengths[17] - fillChars[1].length() + 1; i++) {
							sb.append(fillChars[2]);
						}
						sb.append(fillChars[1]);
						sb.append("\n");
						return sb;
					}

					public boolean isTableEmpty() {
						if (list.size() > 1)
							return false;
						return true;
					}
				}
				Util_tLogRow_1 util_tLogRow_1 = new Util_tLogRow_1();
				util_tLogRow_1.setTableName("tLogRow_1");
				util_tLogRow_1.addRow(new String[] { "ID", "Last_Name", "First_Name", "Middle_Name", "Suffix", "SSN",
						"Date_Of_Birth", "Address1", "Address2", "City", "State", "Zip", "Email", "Registration_Date",
						"Revenue", "Phone", "CreditCard", "ORIGINAL_MARK", });
				StringBuilder strBuffer_tLogRow_1 = null;
				int nb_line_tLogRow_1 = 0;
///////////////////////    			

				/**
				 * [tLogRow_1 begin ] stop
				 */

				/**
				 * [tDataEncrypt_1 begin ] start
				 */

				sh("tDataEncrypt_1");

				s(currentComponent = "tDataEncrypt_1");

				cLabel = "tDataEncrypt_1<br><b>Generate File<br>First</b>";

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tDataEncrypt_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDataEncrypt_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDataEncrypt_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDataEncrypt_1 = new StringBuilder();
							log4jParamters_tDataEncrypt_1.append("Parameters:");
							log4jParamters_tDataEncrypt_1.append("SECRET_TYPE" + " = " + "CRYPTO_FILE");
							log4jParamters_tDataEncrypt_1.append(" | ");
							log4jParamters_tDataEncrypt_1.append("PASSWORD_M1" + " = " + String.valueOf(
									"enc:routine.encryption.key.v1:Ip585o6aLgUbF5ug1kjMM43rBuAn3GgTNaO6BwRVAz8wz/Va")
									.substring(0, 4) + "...");
							log4jParamters_tDataEncrypt_1.append(" | ");
							log4jParamters_tDataEncrypt_1.append("CRYPTO_FILE_PATH" + " = " + "context.CyrptoFilePath");
							log4jParamters_tDataEncrypt_1.append(" | ");
							log4jParamters_tDataEncrypt_1.append("GEN_CRYPTO_FILE_CONTROLLER" + " = " + "false");
							log4jParamters_tDataEncrypt_1.append(" | ");
							log4jParamters_tDataEncrypt_1.append("TABLE_CONFIG" + " = " + "[{COLUMNS_ENCRYPT="
									+ ("false") + ", SCHEMA_COLUMN=" + ("ID") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Last_Name") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("First_Name") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Middle_Name") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Suffix") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("SSN") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Date_Of_Birth") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Address1") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Address2") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("City") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("State") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Zip") + "}, {COLUMNS_ENCRYPT=" + ("true")
									+ ", SCHEMA_COLUMN=" + ("Email") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Registration_Date") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Revenue") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Phone") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("CreditCard") + "}, {COLUMNS_ENCRYPT=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("ORIGINAL_MARK") + "}]");
							log4jParamters_tDataEncrypt_1.append(" | ");
							log4jParamters_tDataEncrypt_1.append("ALGO_VERSION" + " = " + "1");
							log4jParamters_tDataEncrypt_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDataEncrypt_1 - " + (log4jParamters_tDataEncrypt_1));
						}
					}
					new BytesLimit65535_tDataEncrypt_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tDataEncrypt_1", "tDataEncrypt_1<br><b>Generate File<br>First</b>",
							"tDataEncrypt");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				class tDataEncryptUtils_tDataEncrypt_1 {
					public void typeMismatch(String message) {
						throw new RuntimeException(message);
					}
				}

				tDataEncryptUtils_tDataEncrypt_1 utils_tDataEncrypt_1 = new tDataEncryptUtils_tDataEncrypt_1();
				// generate file

				if (true && (context.CyrptoFilePath == null || "".equals(context.CyrptoFilePath.trim()))) {
					throw new RuntimeException(
							"[ERROR]: The cryptographic file is missing. Ensure it has been generated and the file path is correct.");
				}

				int nb_line_tDataEncrypt_1 = 0;
				// TODO maybe check if the file
				// exists"enc:routine.encryption.key.v1:g+PuSMlTdmieEBTDlOnyOmz+P4NF/bV6sUv6lPpW/1ICxP1R"),
				// "AES", context.CyrptoFilePath, 1);

				org.talend.dataquality.encryption.EncryptionService encryptionService_tDataEncrypt_1;
				encryptionService_tDataEncrypt_1 = new org.talend.dataquality.encryption.EncryptionService(
						routines.system.PasswordEncryptUtil.decryptPassword(
								"enc:routine.encryption.key.v1:g+PuSMlTdmieEBTDlOnyOmz+P4NF/bV6sUv6lPpW/1ICxP1R"),
						context.CyrptoFilePath, true, 1);

				/**
				 * [tDataEncrypt_1 begin ] stop
				 */

				/**
				 * [tFileInputDelimited_1 begin ] start
				 */

				sh("tFileInputDelimited_1");

				s(currentComponent = "tFileInputDelimited_1");

				cLabel = "Clients";

				int tos_count_tFileInputDelimited_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileInputDelimited_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileInputDelimited_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileInputDelimited_1 = new StringBuilder();
							log4jParamters_tFileInputDelimited_1.append("Parameters:");
							log4jParamters_tFileInputDelimited_1.append("USE_EXISTING_DYNAMIC" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1
									.append("FILENAME" + " = " + "context.CLIENTS_FILE_LOC");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("FIELDSEPARATOR" + " = " + "\",\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("HEADER" + " = " + "1");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("FOOTER" + " = " + "0");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("LIMIT" + " = " + "10");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("REMOVE_EMPTY_ROW" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("UNCOMPRESS" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("DIE_ON_ERROR" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ADVANCED_SEPARATOR" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("RANDOM" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("TRIMALL" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("TRIMSELECT" + " = " + "[{TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("ID") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("Last_Name") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("First_Name")
									+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Middle_Name") + "}, {TRIM="
									+ ("false") + ", SCHEMA_COLUMN=" + ("Suffix") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("SSN") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("Date_Of_Birth") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Address1")
									+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Address2") + "}, {TRIM="
									+ ("false") + ", SCHEMA_COLUMN=" + ("City") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("State") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("Zip") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Email") + "}, {TRIM="
									+ ("false") + ", SCHEMA_COLUMN=" + ("Registration_Date") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("Revenue") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("Phone") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("CreditCard") + "}]");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CHECK_FIELDS_NUM" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CHECK_DATE" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ENCODING" + " = " + "\"US-ASCII\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("SPLITRECORD" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ENABLE_DECODE" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("USE_HEADER_AS_IS" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileInputDelimited_1 - " + (log4jParamters_tFileInputDelimited_1));
						}
					}
					new BytesLimit65535_tFileInputDelimited_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileInputDelimited_1", "Clients", "tFileInputDelimited");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				final routines.system.RowState rowstate_tFileInputDelimited_1 = new routines.system.RowState();

				int nb_line_tFileInputDelimited_1 = 0;
				org.talend.fileprocess.FileInputDelimited fid_tFileInputDelimited_1 = null;
				int limit_tFileInputDelimited_1 = 10;
				try {

					Object filename_tFileInputDelimited_1 = context.CLIENTS_FILE_LOC;
					if (filename_tFileInputDelimited_1 instanceof java.io.InputStream) {

						int footer_value_tFileInputDelimited_1 = 0, random_value_tFileInputDelimited_1 = -1;
						if (footer_value_tFileInputDelimited_1 > 0 || random_value_tFileInputDelimited_1 > 0) {
							throw new java.lang.Exception(
									"When the input source is a stream,footer and random shouldn't be bigger than 0.");
						}

					}
					try {
						fid_tFileInputDelimited_1 = new org.talend.fileprocess.FileInputDelimited(
								context.CLIENTS_FILE_LOC, "US-ASCII", ",", "\n", false, 1, 0,
								limit_tFileInputDelimited_1, -1, false);
					} catch (java.lang.Exception e) {
						globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE", e.getMessage());

						log.error("tFileInputDelimited_1 - " + e.getMessage());

						System.err.println(e.getMessage());

					}

					log.info("tFileInputDelimited_1 - Retrieving records from the datasource.");

					while (fid_tFileInputDelimited_1 != null && fid_tFileInputDelimited_1.nextRecord()) {
						rowstate_tFileInputDelimited_1.reset();

						row1 = null;

						boolean whetherReject_tFileInputDelimited_1 = false;
						row1 = new row1Struct();
						try {

							int columnIndexWithD_tFileInputDelimited_1 = 0;

							String temp = "";

							columnIndexWithD_tFileInputDelimited_1 = 0;

							temp = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
							if (temp.length() > 0) {

								try {

									row1.ID = ParserUtils.parseTo_Integer(temp);

								} catch (java.lang.Exception ex_tFileInputDelimited_1) {
									globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE",
											ex_tFileInputDelimited_1.getMessage());
									rowstate_tFileInputDelimited_1.setException(new RuntimeException(String.format(
											"Couldn't parse value for column '%s' in '%s', value is '%s'. Details: %s",
											"ID", "row1", temp, ex_tFileInputDelimited_1), ex_tFileInputDelimited_1));
								}

							} else {

								row1.ID = null;

							}

							columnIndexWithD_tFileInputDelimited_1 = 1;

							row1.Last_Name = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 2;

							row1.First_Name = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 3;

							row1.Middle_Name = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 4;

							row1.Suffix = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 5;

							row1.SSN = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 6;

							temp = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
							if (temp.length() > 0) {

								try {

									row1.Date_Of_Birth = ParserUtils.parseTo_Date(temp, "dd-MM-yyyy");

								} catch (java.lang.Exception ex_tFileInputDelimited_1) {
									globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE",
											ex_tFileInputDelimited_1.getMessage());
									rowstate_tFileInputDelimited_1.setException(new RuntimeException(String.format(
											"Couldn't parse value for column '%s' in '%s', value is '%s'. Details: %s",
											"Date_Of_Birth", "row1", temp, ex_tFileInputDelimited_1),
											ex_tFileInputDelimited_1));
								}

							} else {

								row1.Date_Of_Birth = null;

							}

							columnIndexWithD_tFileInputDelimited_1 = 7;

							row1.Address1 = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 8;

							row1.Address2 = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 9;

							row1.City = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 10;

							row1.State = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 11;

							temp = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);
							if (temp.length() > 0) {

								try {

									row1.Zip = ParserUtils.parseTo_Integer(temp);

								} catch (java.lang.Exception ex_tFileInputDelimited_1) {
									globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE",
											ex_tFileInputDelimited_1.getMessage());
									rowstate_tFileInputDelimited_1.setException(new RuntimeException(String.format(
											"Couldn't parse value for column '%s' in '%s', value is '%s'. Details: %s",
											"Zip", "row1", temp, ex_tFileInputDelimited_1), ex_tFileInputDelimited_1));
								}

							} else {

								row1.Zip = null;

							}

							columnIndexWithD_tFileInputDelimited_1 = 12;

							row1.Email = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 13;

							row1.Registration_Date = fid_tFileInputDelimited_1
									.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 14;

							row1.Revenue = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 15;

							row1.Phone = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 16;

							row1.CreditCard = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							if (rowstate_tFileInputDelimited_1.getException() != null) {
								throw rowstate_tFileInputDelimited_1.getException();
							}

						} catch (java.lang.Exception e) {
							globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE", e.getMessage());
							whetherReject_tFileInputDelimited_1 = true;

							log.error("tFileInputDelimited_1 - " + e.getMessage());

							System.err.println(e.getMessage());
							row1 = null;

						}

						log.debug("tFileInputDelimited_1 - Retrieving the record "
								+ fid_tFileInputDelimited_1.getRowNumber() + ".");

						/**
						 * [tFileInputDelimited_1 begin ] stop
						 */

						/**
						 * [tFileInputDelimited_1 main ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "Clients";

						tos_count_tFileInputDelimited_1++;

						/**
						 * [tFileInputDelimited_1 main ] stop
						 */

						/**
						 * [tFileInputDelimited_1 process_data_begin ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "Clients";

						/**
						 * [tFileInputDelimited_1 process_data_begin ] stop
						 */

// Start of branch "row1"
						if (row1 != null) {

							/**
							 * [tDataEncrypt_1 main ] start
							 */

							s(currentComponent = "tDataEncrypt_1");

							cLabel = "tDataEncrypt_1<br><b>Generate File<br>First</b>";

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row1", "tFileInputDelimited_1", "Clients", "tFileInputDelimited",
									"tDataEncrypt_1", "tDataEncrypt_1<br><b>Generate File<br>First</b>", "tDataEncrypt"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
							}

//not to transliterat the input column: ID
							row2.ID = row1.ID;
							row2.Last_Name = row1.Last_Name == null ? null
									: encryptionService_tDataEncrypt_1.encrypt(row1.Last_Name);
							row2.First_Name = row1.First_Name == null ? null
									: encryptionService_tDataEncrypt_1.encrypt(row1.First_Name);
							row2.Middle_Name = row1.Middle_Name == null ? null
									: encryptionService_tDataEncrypt_1.encrypt(row1.Middle_Name);
//not to transliterat the input column: Suffix
							row2.Suffix = row1.Suffix;
							row2.SSN = row1.SSN == null ? null : encryptionService_tDataEncrypt_1.encrypt(row1.SSN);
//not to transliterat the input column: Date_Of_Birth
							row2.Date_Of_Birth = row1.Date_Of_Birth;
							row2.Address1 = row1.Address1 == null ? null
									: encryptionService_tDataEncrypt_1.encrypt(row1.Address1);
							row2.Address2 = row1.Address2 == null ? null
									: encryptionService_tDataEncrypt_1.encrypt(row1.Address2);
//not to transliterat the input column: City
							row2.City = row1.City;
//not to transliterat the input column: State
							row2.State = row1.State;
//not to transliterat the input column: Zip
							row2.Zip = row1.Zip;
							row2.Email = row1.Email == null ? null
									: encryptionService_tDataEncrypt_1.encrypt(row1.Email);
//not to transliterat the input column: Registration_Date
							row2.Registration_Date = row1.Registration_Date;
//not to transliterat the input column: Revenue
							row2.Revenue = row1.Revenue;
//not to transliterat the input column: Phone
							row2.Phone = row1.Phone;
//not to transliterat the input column: CreditCard
							row2.CreditCard = row1.CreditCard;
							nb_line_tDataEncrypt_1++;

							tos_count_tDataEncrypt_1++;

							/**
							 * [tDataEncrypt_1 main ] stop
							 */

							/**
							 * [tDataEncrypt_1 process_data_begin ] start
							 */

							s(currentComponent = "tDataEncrypt_1");

							cLabel = "tDataEncrypt_1<br><b>Generate File<br>First</b>";

							/**
							 * [tDataEncrypt_1 process_data_begin ] stop
							 */

							/**
							 * [tLogRow_1 main ] start
							 */

							s(currentComponent = "tLogRow_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row2", "tDataEncrypt_1", "tDataEncrypt_1<br><b>Generate File<br>First</b>",
									"tDataEncrypt", "tLogRow_1", "tLogRow_1", "tLogRow"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row2 - " + (row2 == null ? "" : row2.toLogString()));
							}

///////////////////////		

							String[] row_tLogRow_1 = new String[18];

							if (row2.ID != null) { //
								row_tLogRow_1[0] = String.valueOf(row2.ID);

							} //

							if (row2.Last_Name != null) { //
								row_tLogRow_1[1] = String.valueOf(row2.Last_Name);

							} //

							if (row2.First_Name != null) { //
								row_tLogRow_1[2] = String.valueOf(row2.First_Name);

							} //

							if (row2.Middle_Name != null) { //
								row_tLogRow_1[3] = String.valueOf(row2.Middle_Name);

							} //

							if (row2.Suffix != null) { //
								row_tLogRow_1[4] = String.valueOf(row2.Suffix);

							} //

							if (row2.SSN != null) { //
								row_tLogRow_1[5] = String.valueOf(row2.SSN);

							} //

							if (row2.Date_Of_Birth != null) { //
								row_tLogRow_1[6] = FormatterUtils.format_Date(row2.Date_Of_Birth, "dd-MM-yyyy");

							} //

							if (row2.Address1 != null) { //
								row_tLogRow_1[7] = String.valueOf(row2.Address1);

							} //

							if (row2.Address2 != null) { //
								row_tLogRow_1[8] = String.valueOf(row2.Address2);

							} //

							if (row2.City != null) { //
								row_tLogRow_1[9] = String.valueOf(row2.City);

							} //

							if (row2.State != null) { //
								row_tLogRow_1[10] = String.valueOf(row2.State);

							} //

							if (row2.Zip != null) { //
								row_tLogRow_1[11] = String.valueOf(row2.Zip);

							} //

							if (row2.Email != null) { //
								row_tLogRow_1[12] = String.valueOf(row2.Email);

							} //

							if (row2.Registration_Date != null) { //
								row_tLogRow_1[13] = String.valueOf(row2.Registration_Date);

							} //

							if (row2.Revenue != null) { //
								row_tLogRow_1[14] = String.valueOf(row2.Revenue);

							} //

							if (row2.Phone != null) { //
								row_tLogRow_1[15] = String.valueOf(row2.Phone);

							} //

							if (row2.CreditCard != null) { //
								row_tLogRow_1[16] = String.valueOf(row2.CreditCard);

							} //

							if (row2.ORIGINAL_MARK != null) { //
								row_tLogRow_1[17] = String.valueOf(row2.ORIGINAL_MARK);

							} //

							util_tLogRow_1.addRow(row_tLogRow_1);
							nb_line_tLogRow_1++;
							log.info("tLogRow_1 - Content of row " + nb_line_tLogRow_1 + ": "
									+ TalendString.unionString("|", row_tLogRow_1));
//////

//////                    

///////////////////////    			

							row3 = row2;

							tos_count_tLogRow_1++;

							/**
							 * [tLogRow_1 main ] stop
							 */

							/**
							 * [tLogRow_1 process_data_begin ] start
							 */

							s(currentComponent = "tLogRow_1");

							/**
							 * [tLogRow_1 process_data_begin ] stop
							 */

							/**
							 * [tDataDecrypt_1 main ] start
							 */

							s(currentComponent = "tDataDecrypt_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row3", "tLogRow_1", "tLogRow_1", "tLogRow", "tDataDecrypt_1", "tDataDecrypt_1",
									"tDataDecrypt"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row3 - " + (row3 == null ? "" : row3.toLogString()));
							}

//not to transliterat the input column: ID
							row4.ID = row3.ID;
							String originalColumnValue_tDataDecrypt_1_1 = "";

							try {
								originalColumnValue_tDataDecrypt_1_1 = row3.Last_Name;
								row4.Last_Name = row3.Last_Name == null ? null
										: ("".equals(row3.Last_Name) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_1));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.Last_Name = originalColumnValue_tDataDecrypt_1_1;
									log.error(
											"Input data can't be decrypted using the given cryptographic file:'Last_Name->"
													+ originalColumnValue_tDataDecrypt_1_1 + "'");

								}
							}

							String originalColumnValue_tDataDecrypt_1_2 = "";

							try {
								originalColumnValue_tDataDecrypt_1_2 = row3.First_Name;
								row4.First_Name = row3.First_Name == null ? null
										: ("".equals(row3.First_Name) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_2));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.First_Name = originalColumnValue_tDataDecrypt_1_2;
									log.error(
											"Input data can't be decrypted using the given cryptographic file:'First_Name->"
													+ originalColumnValue_tDataDecrypt_1_2 + "'");

								}
							}

							String originalColumnValue_tDataDecrypt_1_3 = "";

							try {
								originalColumnValue_tDataDecrypt_1_3 = row3.Middle_Name;
								row4.Middle_Name = row3.Middle_Name == null ? null
										: ("".equals(row3.Middle_Name) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_3));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.Middle_Name = originalColumnValue_tDataDecrypt_1_3;
									log.error(
											"Input data can't be decrypted using the given cryptographic file:'Middle_Name->"
													+ originalColumnValue_tDataDecrypt_1_3 + "'");

								}
							}

//not to transliterat the input column: Suffix
							row4.Suffix = row3.Suffix;
							String originalColumnValue_tDataDecrypt_1_5 = "";

							try {
								originalColumnValue_tDataDecrypt_1_5 = row3.SSN;
								row4.SSN = row3.SSN == null ? null
										: ("".equals(row3.SSN) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_5));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.SSN = originalColumnValue_tDataDecrypt_1_5;
									log.error("Input data can't be decrypted using the given cryptographic file:'SSN->"
											+ originalColumnValue_tDataDecrypt_1_5 + "'");

								}
							}

//not to transliterat the input column: Date_Of_Birth
							row4.Date_Of_Birth = row3.Date_Of_Birth;
							String originalColumnValue_tDataDecrypt_1_7 = "";

							try {
								originalColumnValue_tDataDecrypt_1_7 = row3.Address1;
								row4.Address1 = row3.Address1 == null ? null
										: ("".equals(row3.Address1) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_7));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.Address1 = originalColumnValue_tDataDecrypt_1_7;
									log.error(
											"Input data can't be decrypted using the given cryptographic file:'Address1->"
													+ originalColumnValue_tDataDecrypt_1_7 + "'");

								}
							}

							String originalColumnValue_tDataDecrypt_1_8 = "";

							try {
								originalColumnValue_tDataDecrypt_1_8 = row3.Address2;
								row4.Address2 = row3.Address2 == null ? null
										: ("".equals(row3.Address2) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_8));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.Address2 = originalColumnValue_tDataDecrypt_1_8;
									log.error(
											"Input data can't be decrypted using the given cryptographic file:'Address2->"
													+ originalColumnValue_tDataDecrypt_1_8 + "'");

								}
							}

//not to transliterat the input column: City
							row4.City = row3.City;
//not to transliterat the input column: State
							row4.State = row3.State;
//not to transliterat the input column: Zip
							row4.Zip = row3.Zip;
							String originalColumnValue_tDataDecrypt_1_12 = "";

							try {
								originalColumnValue_tDataDecrypt_1_12 = row3.Email;
								row4.Email = row3.Email == null ? null
										: ("".equals(row3.Email) ? ""
												: encryptionService_tDataDecrypt_1
														.decrypt(originalColumnValue_tDataDecrypt_1_12));
							} catch (org.talend.dataquality.encryption.exception.IncorrectCryptoFileException e) {
								// if die on error,stop the job and throw an error.
								if (true) {
									throw new org.talend.dataquality.encryption.exception.IncorrectCryptoFileException(
											e.getMessage(), e);
								} else {
									row4.Email = originalColumnValue_tDataDecrypt_1_12;
									log.error(
											"Input data can't be decrypted using the given cryptographic file:'Email->"
													+ originalColumnValue_tDataDecrypt_1_12 + "'");

								}
							}

//not to transliterat the input column: Registration_Date
							row4.Registration_Date = row3.Registration_Date;
//not to transliterat the input column: Revenue
							row4.Revenue = row3.Revenue;
//not to transliterat the input column: Phone
							row4.Phone = row3.Phone;
//not to transliterat the input column: CreditCard
							row4.CreditCard = row3.CreditCard;
//not to transliterat the input column: ORIGINAL_MARK
							row4.ORIGINAL_MARK = row3.ORIGINAL_MARK;
							nb_line_tDataDecrypt_1++;

							tos_count_tDataDecrypt_1++;

							/**
							 * [tDataDecrypt_1 main ] stop
							 */

							/**
							 * [tDataDecrypt_1 process_data_begin ] start
							 */

							s(currentComponent = "tDataDecrypt_1");

							/**
							 * [tDataDecrypt_1 process_data_begin ] stop
							 */

							/**
							 * [tLogRow_2 main ] start
							 */

							s(currentComponent = "tLogRow_2");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row4", "tDataDecrypt_1", "tDataDecrypt_1", "tDataDecrypt", "tLogRow_2",
									"tLogRow_2", "tLogRow"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row4 - " + (row4 == null ? "" : row4.toLogString()));
							}

///////////////////////		

							String[] row_tLogRow_2 = new String[18];

							if (row4.ID != null) { //
								row_tLogRow_2[0] = String.valueOf(row4.ID);

							} //

							if (row4.Last_Name != null) { //
								row_tLogRow_2[1] = String.valueOf(row4.Last_Name);

							} //

							if (row4.First_Name != null) { //
								row_tLogRow_2[2] = String.valueOf(row4.First_Name);

							} //

							if (row4.Middle_Name != null) { //
								row_tLogRow_2[3] = String.valueOf(row4.Middle_Name);

							} //

							if (row4.Suffix != null) { //
								row_tLogRow_2[4] = String.valueOf(row4.Suffix);

							} //

							if (row4.SSN != null) { //
								row_tLogRow_2[5] = String.valueOf(row4.SSN);

							} //

							if (row4.Date_Of_Birth != null) { //
								row_tLogRow_2[6] = FormatterUtils.format_Date(row4.Date_Of_Birth, "dd-MM-yyyy");

							} //

							if (row4.Address1 != null) { //
								row_tLogRow_2[7] = String.valueOf(row4.Address1);

							} //

							if (row4.Address2 != null) { //
								row_tLogRow_2[8] = String.valueOf(row4.Address2);

							} //

							if (row4.City != null) { //
								row_tLogRow_2[9] = String.valueOf(row4.City);

							} //

							if (row4.State != null) { //
								row_tLogRow_2[10] = String.valueOf(row4.State);

							} //

							if (row4.Zip != null) { //
								row_tLogRow_2[11] = String.valueOf(row4.Zip);

							} //

							if (row4.Email != null) { //
								row_tLogRow_2[12] = String.valueOf(row4.Email);

							} //

							if (row4.Registration_Date != null) { //
								row_tLogRow_2[13] = String.valueOf(row4.Registration_Date);

							} //

							if (row4.Revenue != null) { //
								row_tLogRow_2[14] = String.valueOf(row4.Revenue);

							} //

							if (row4.Phone != null) { //
								row_tLogRow_2[15] = String.valueOf(row4.Phone);

							} //

							if (row4.CreditCard != null) { //
								row_tLogRow_2[16] = String.valueOf(row4.CreditCard);

							} //

							if (row4.ORIGINAL_MARK != null) { //
								row_tLogRow_2[17] = String.valueOf(row4.ORIGINAL_MARK);

							} //

							util_tLogRow_2.addRow(row_tLogRow_2);
							nb_line_tLogRow_2++;
							log.info("tLogRow_2 - Content of row " + nb_line_tLogRow_2 + ": "
									+ TalendString.unionString("|", row_tLogRow_2));
//////

//////                    

///////////////////////    			

							tos_count_tLogRow_2++;

							/**
							 * [tLogRow_2 main ] stop
							 */

							/**
							 * [tLogRow_2 process_data_begin ] start
							 */

							s(currentComponent = "tLogRow_2");

							/**
							 * [tLogRow_2 process_data_begin ] stop
							 */

							/**
							 * [tLogRow_2 process_data_end ] start
							 */

							s(currentComponent = "tLogRow_2");

							/**
							 * [tLogRow_2 process_data_end ] stop
							 */

							/**
							 * [tDataDecrypt_1 process_data_end ] start
							 */

							s(currentComponent = "tDataDecrypt_1");

							/**
							 * [tDataDecrypt_1 process_data_end ] stop
							 */

							/**
							 * [tLogRow_1 process_data_end ] start
							 */

							s(currentComponent = "tLogRow_1");

							/**
							 * [tLogRow_1 process_data_end ] stop
							 */

							/**
							 * [tDataEncrypt_1 process_data_end ] start
							 */

							s(currentComponent = "tDataEncrypt_1");

							cLabel = "tDataEncrypt_1<br><b>Generate File<br>First</b>";

							/**
							 * [tDataEncrypt_1 process_data_end ] stop
							 */

						} // End of branch "row1"

						/**
						 * [tFileInputDelimited_1 process_data_end ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "Clients";

						/**
						 * [tFileInputDelimited_1 process_data_end ] stop
						 */

						/**
						 * [tFileInputDelimited_1 end ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "Clients";

					}
				} finally {
					if (!((Object) (context.CLIENTS_FILE_LOC) instanceof java.io.InputStream)) {
						if (fid_tFileInputDelimited_1 != null) {
							fid_tFileInputDelimited_1.close();
						}
					}
					if (fid_tFileInputDelimited_1 != null) {
						globalMap.put("tFileInputDelimited_1_NB_LINE", fid_tFileInputDelimited_1.getRowNumber());

						log.info("tFileInputDelimited_1 - Retrieved records count: "
								+ fid_tFileInputDelimited_1.getRowNumber() + ".");

					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileInputDelimited_1 - " + ("Done."));

				ok_Hash.put("tFileInputDelimited_1", true);
				end_Hash.put("tFileInputDelimited_1", System.currentTimeMillis());

				/**
				 * [tFileInputDelimited_1 end ] stop
				 */

				/**
				 * [tDataEncrypt_1 end ] start
				 */

				s(currentComponent = "tDataEncrypt_1");

				cLabel = "tDataEncrypt_1<br><b>Generate File<br>First</b>";

				globalMap.put("tDataEncrypt_1_NB_LINE", nb_line_tDataEncrypt_1);
				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tFileInputDelimited_1", "Clients", "tFileInputDelimited", "tDataEncrypt_1",
						"tDataEncrypt_1<br><b>Generate File<br>First</b>", "tDataEncrypt", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tDataEncrypt_1 - " + ("Done."));

				ok_Hash.put("tDataEncrypt_1", true);
				end_Hash.put("tDataEncrypt_1", System.currentTimeMillis());

				/**
				 * [tDataEncrypt_1 end ] stop
				 */

				/**
				 * [tLogRow_1 end ] start
				 */

				s(currentComponent = "tLogRow_1");

//////

				java.io.PrintStream consoleOut_tLogRow_1 = null;
				if (globalMap.get("tLogRow_CONSOLE") != null) {
					consoleOut_tLogRow_1 = (java.io.PrintStream) globalMap.get("tLogRow_CONSOLE");
				} else {
					consoleOut_tLogRow_1 = new java.io.PrintStream(new java.io.BufferedOutputStream(System.out));
					globalMap.put("tLogRow_CONSOLE", consoleOut_tLogRow_1);
				}

				consoleOut_tLogRow_1.println(util_tLogRow_1.format().toString());
				consoleOut_tLogRow_1.flush();
//////
				globalMap.put("tLogRow_1_NB_LINE", nb_line_tLogRow_1);
				if (log.isInfoEnabled())
					log.info("tLogRow_1 - " + ("Printed row count: ") + (nb_line_tLogRow_1) + ("."));

///////////////////////    			

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row2", 2, 0,
						"tDataEncrypt_1", "tDataEncrypt_1<br><b>Generate File<br>First</b>", "tDataEncrypt",
						"tLogRow_1", "tLogRow_1", "tLogRow", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + ("Done."));

				ok_Hash.put("tLogRow_1", true);
				end_Hash.put("tLogRow_1", System.currentTimeMillis());

				/**
				 * [tLogRow_1 end ] stop
				 */

				/**
				 * [tDataDecrypt_1 end ] start
				 */

				s(currentComponent = "tDataDecrypt_1");

				globalMap.put("tDataDecrypt_1_NB_LINE", nb_line_tDataDecrypt_1);
				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row3", 2, 0,
						"tLogRow_1", "tLogRow_1", "tLogRow", "tDataDecrypt_1", "tDataDecrypt_1", "tDataDecrypt",
						"output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tDataDecrypt_1 - " + ("Done."));

				ok_Hash.put("tDataDecrypt_1", true);
				end_Hash.put("tDataDecrypt_1", System.currentTimeMillis());

				/**
				 * [tDataDecrypt_1 end ] stop
				 */

				/**
				 * [tLogRow_2 end ] start
				 */

				s(currentComponent = "tLogRow_2");

//////

				java.io.PrintStream consoleOut_tLogRow_2 = null;
				if (globalMap.get("tLogRow_CONSOLE") != null) {
					consoleOut_tLogRow_2 = (java.io.PrintStream) globalMap.get("tLogRow_CONSOLE");
				} else {
					consoleOut_tLogRow_2 = new java.io.PrintStream(new java.io.BufferedOutputStream(System.out));
					globalMap.put("tLogRow_CONSOLE", consoleOut_tLogRow_2);
				}

				consoleOut_tLogRow_2.println(util_tLogRow_2.format().toString());
				consoleOut_tLogRow_2.flush();
//////
				globalMap.put("tLogRow_2_NB_LINE", nb_line_tLogRow_2);
				if (log.isInfoEnabled())
					log.info("tLogRow_2 - " + ("Printed row count: ") + (nb_line_tLogRow_2) + ("."));

///////////////////////    			

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row4", 2, 0,
						"tDataDecrypt_1", "tDataDecrypt_1", "tDataDecrypt", "tLogRow_2", "tLogRow_2", "tLogRow",
						"output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tLogRow_2 - " + ("Done."));

				ok_Hash.put("tLogRow_2", true);
				end_Hash.put("tLogRow_2", System.currentTimeMillis());

				/**
				 * [tLogRow_2 end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [tFileInputDelimited_1 finally ] start
				 */

				s(currentComponent = "tFileInputDelimited_1");

				cLabel = "Clients";

				/**
				 * [tFileInputDelimited_1 finally ] stop
				 */

				/**
				 * [tDataEncrypt_1 finally ] start
				 */

				s(currentComponent = "tDataEncrypt_1");

				cLabel = "tDataEncrypt_1<br><b>Generate File<br>First</b>";

				/**
				 * [tDataEncrypt_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				s(currentComponent = "tLogRow_1");

				/**
				 * [tLogRow_1 finally ] stop
				 */

				/**
				 * [tDataDecrypt_1 finally ] start
				 */

				s(currentComponent = "tDataDecrypt_1");

				/**
				 * [tDataDecrypt_1 finally ] stop
				 */

				/**
				 * [tLogRow_2 finally ] start
				 */

				s(currentComponent = "tLogRow_2");

				/**
				 * [tLogRow_2 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 1);
	}

	public void talendJobLogProcess(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("talendJobLog_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		String iterateId = "";

		String currentComponent = "";
		s("none");
		String cLabel = null;
		java.util.Map<String, Object> resourceMap = new java.util.HashMap<String, Object>();

		try {
			// TDI-39566 avoid throwing an useless Exception
			boolean resumeIt = true;
			if (globalResumeTicket == false && resumeEntryMethodName != null) {
				String currentMethodName = new java.lang.Exception().getStackTrace()[0].getMethodName();
				resumeIt = resumeEntryMethodName.equals(currentMethodName);
			}
			if (resumeIt || globalResumeTicket) { // start the resume
				globalResumeTicket = true;

				/**
				 * [talendJobLog begin ] start
				 */

				sh("talendJobLog");

				s(currentComponent = "talendJobLog");

				int tos_count_talendJobLog = 0;

				for (JobStructureCatcherUtils.JobStructureCatcherMessage jcm : talendJobLog.getMessages()) {
					org.talend.job.audit.JobContextBuilder builder_talendJobLog = org.talend.job.audit.JobContextBuilder
							.create().jobName(jcm.job_name).jobId(jcm.job_id).jobVersion(jcm.job_version)
							.custom("process_id", jcm.pid).custom("thread_id", jcm.tid).custom("pid", pid)
							.custom("father_pid", fatherPid).custom("root_pid", rootPid);
					org.talend.logging.audit.Context log_context_talendJobLog = null;

					if (jcm.log_type == JobStructureCatcherUtils.LogType.PERFORMANCE) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.sourceId(jcm.sourceId)
								.sourceLabel(jcm.sourceLabel).sourceConnectorType(jcm.sourceComponentName)
								.targetId(jcm.targetId).targetLabel(jcm.targetLabel)
								.targetConnectorType(jcm.targetComponentName).connectionName(jcm.current_connector)
								.rows(jcm.row_count).duration(duration).build();
						auditLogger_talendJobLog.flowExecution(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBSTART) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).build();
						auditLogger_talendJobLog.jobstart(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBEND) {
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment).duration(duration)
								.status(jcm.status).build();
						auditLogger_talendJobLog.jobstop(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.RUNCOMPONENT) {
						log_context_talendJobLog = builder_talendJobLog.timestamp(jcm.moment)
								.connectorType(jcm.component_name).connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label).build();
						auditLogger_talendJobLog.runcomponent(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWINPUT) {// log current component
																							// input line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowInput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.FLOWOUTPUT) {// log current component
																								// output/reject line
						long timeMS = jcm.end_time - jcm.start_time;
						String duration = String.valueOf(timeMS);

						log_context_talendJobLog = builder_talendJobLog.connectorType(jcm.component_name)
								.connectorId(jcm.component_id).connectorLabel(jcm.component_label)
								.connectionName(jcm.current_connector).connectionType(jcm.current_connector_type)
								.rows(jcm.total_row_number).duration(duration).build();
						auditLogger_talendJobLog.flowOutput(log_context_talendJobLog);
					} else if (jcm.log_type == JobStructureCatcherUtils.LogType.JOBERROR) {
						java.lang.Exception e_talendJobLog = jcm.exception;
						if (e_talendJobLog != null) {
							try (java.io.StringWriter sw_talendJobLog = new java.io.StringWriter();
									java.io.PrintWriter pw_talendJobLog = new java.io.PrintWriter(sw_talendJobLog)) {
								e_talendJobLog.printStackTrace(pw_talendJobLog);
								builder_talendJobLog.custom("stacktrace", sw_talendJobLog.getBuffer().substring(0,
										java.lang.Math.min(sw_talendJobLog.getBuffer().length(), 512)));
							}
						}

						if (jcm.extra_info != null) {
							builder_talendJobLog.connectorId(jcm.component_id).custom("extra_info", jcm.extra_info);
						}

						log_context_talendJobLog = builder_talendJobLog
								.connectorType(jcm.component_id.substring(0, jcm.component_id.lastIndexOf('_')))
								.connectorId(jcm.component_id)
								.connectorLabel(jcm.component_label == null ? jcm.component_id : jcm.component_label)
								.build();

						auditLogger_talendJobLog.exception(log_context_talendJobLog);
					}

				}

				/**
				 * [talendJobLog begin ] stop
				 */

				/**
				 * [talendJobLog main ] start
				 */

				s(currentComponent = "talendJobLog");

				tos_count_talendJobLog++;

				/**
				 * [talendJobLog main ] stop
				 */

				/**
				 * [talendJobLog process_data_begin ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog process_data_begin ] stop
				 */

				/**
				 * [talendJobLog process_data_end ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog process_data_end ] stop
				 */

				/**
				 * [talendJobLog end ] start
				 */

				s(currentComponent = "talendJobLog");

				ok_Hash.put("talendJobLog", true);
				end_Hash.put("talendJobLog", System.currentTimeMillis());

				/**
				 * [talendJobLog end ] stop
				 */

			} // end the resume

		} catch (java.lang.Exception e) {

			if (!(e instanceof TalendException)) {
				log.fatal(currentComponent + " " + e.getMessage(), e);
			}

			TalendException te = new TalendException(e, currentComponent, cLabel, globalMap);

			throw te;
		} catch (java.lang.Error error) {

			runStat.stopThreadStat();

			throw error;
		} finally {

			try {

				/**
				 * [talendJobLog finally ] start
				 */

				s(currentComponent = "talendJobLog");

				/**
				 * [talendJobLog finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("talendJobLog_SUBPROCESS_STATE", 1);
	}

	public String resuming_logs_dir_path = null;
	public String resuming_checkpoint_path = null;
	public String parent_part_launcher = null;
	private String resumeEntryMethodName = null;
	private boolean globalResumeTicket = false;

	public boolean watch = false;
	// portStats is null, it means don't execute the statistics
	public Integer portStats = null;
	public int portTraces = 4334;
	public String clientHost;
	public String defaultClientHost = "localhost";
	public String contextStr = "Default";
	public boolean isDefaultContext = true;
	public String pid = "0";
	public String rootPid = null;
	public String fatherPid = null;
	public String fatherNode = null;
	public long startTime = 0;
	public boolean isChildJob = false;
	public String log4jLevel = "";

	private boolean enableLogStash;
	private boolean enableLineage;

	private boolean execStat = true;

	private ThreadLocal<java.util.Map<String, String>> threadLocal = new ThreadLocal<java.util.Map<String, String>>() {
		protected java.util.Map<String, String> initialValue() {
			java.util.Map<String, String> threadRunResultMap = new java.util.HashMap<String, String>();
			threadRunResultMap.put("errorCode", null);
			threadRunResultMap.put("status", "");
			return threadRunResultMap;
		};
	};

	protected PropertiesWithType context_param = new PropertiesWithType();
	public java.util.Map<String, Object> parentContextMap = new java.util.HashMap<String, Object>();

	public String status = "";

	private final static java.util.Properties jobInfo = new java.util.Properties();
	private final static java.util.Map<String, String> mdcInfo = new java.util.HashMap<>();
	private final static java.util.concurrent.atomic.AtomicLong subJobPidCounter = new java.util.concurrent.atomic.AtomicLong();

	public static void main(String[] args) {
		final encrypt_and_decrypt encrypt_and_decryptClass = new encrypt_and_decrypt();

		int exitCode = encrypt_and_decryptClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'encrypt_and_decrypt' - Done.");
		}

		System.exit(exitCode);
	}

	private void getjobInfo() {
		final String TEMPLATE_PATH = "src/main/templates/jobInfo_template.properties";
		final String BUILD_PATH = "../jobInfo.properties";
		final String path = this.getClass().getResource("").getPath();
		if (path.lastIndexOf("target") > 0) {
			final java.io.File templateFile = new java.io.File(
					path.substring(0, path.lastIndexOf("target")).concat(TEMPLATE_PATH));
			if (templateFile.exists()) {
				readJobInfo(templateFile);
				return;
			}
		}
		readJobInfo(new java.io.File(BUILD_PATH));
	}

	private void readJobInfo(java.io.File jobInfoFile) {

		if (jobInfoFile.exists()) {
			try (java.io.InputStream is = new java.io.FileInputStream(jobInfoFile)) {
				jobInfo.load(is);
			} catch (IOException e) {

				log.debug("Read jobInfo.properties file fail: " + e.getMessage());

			}
		}
		log.info(String.format("Project name: %s\tJob name: %s\tGIT Commit ID: %s\tTalend Version: %s", projectName,
				jobName, jobInfo.getProperty("gitCommitId"), "8.0.1.20250625_0954-patch"));

	}

	public String[][] runJob(String[] args) {

		int exitCode = runJobInTOS(args);
		String[][] bufferValue = new String[][] { { Integer.toString(exitCode) } };

		return bufferValue;
	}

	public boolean hastBufferOutputComponent() {
		boolean hastBufferOutput = false;

		return hastBufferOutput;
	}

	public int runJobInTOS(String[] args) {
		// reset status
		status = "";

		String lastStr = "";
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--context_param")) {
				lastStr = arg;
			} else if (lastStr.equals("")) {
				evalParam(arg);
			} else {
				evalParam(lastStr + " " + arg);
				lastStr = "";
			}
		}

		final boolean enableCBP = false;
		boolean inOSGi = routines.system.BundleUtils.inOSGi();

		if (!inOSGi && isCBPClientPresent) {
			if (org.talend.metrics.CBPClient.getInstanceForCurrentVM() == null) {
				try {
					org.talend.metrics.CBPClient.startListenIfNotStarted(enableCBP, true);
				} catch (java.lang.Exception e) {
					errorCode = 1;
					status = "failure";
					e.printStackTrace();
					return 1;
				}
			}
		}

		enableLogStash = "true".equalsIgnoreCase(System.getProperty("audit.enabled"));

		if (!"".equals(log4jLevel)) {

			if ("trace".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.TRACE);
			} else if ("debug".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.DEBUG);
			} else if ("info".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.INFO);
			} else if ("warn".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.WARN);
			} else if ("error".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.ERROR);
			} else if ("fatal".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.FATAL);
			} else if ("off".equalsIgnoreCase(log4jLevel)) {
				org.apache.logging.log4j.core.config.Configurator.setLevel(log.getName(),
						org.apache.logging.log4j.Level.OFF);
			}
			org.apache.logging.log4j.core.config.Configurator
					.setLevel(org.apache.logging.log4j.LogManager.getRootLogger().getName(), log.getLevel());

		}

		getjobInfo();
		log.info("TalendJob: 'encrypt_and_decrypt' - Start.");

		java.util.Set<Object> jobInfoKeys = jobInfo.keySet();
		for (Object jobInfoKey : jobInfoKeys) {
			org.slf4j.MDC.put("_" + jobInfoKey.toString(), jobInfo.get(jobInfoKey).toString());
		}
		org.slf4j.MDC.put("_pid", pid);
		org.slf4j.MDC.put("_rootPid", rootPid);
		org.slf4j.MDC.put("_fatherPid", fatherPid);
		org.slf4j.MDC.put("_projectName", projectName);
		org.slf4j.MDC.put("_startTimestamp", java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC)
				.format(java.time.format.DateTimeFormatter.ISO_INSTANT));
		org.slf4j.MDC.put("_jobRepositoryId", "_ERHsAKJjEemUjYpcVKCicA");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2025-06-30T22:04:44.732762900Z");

		java.lang.management.RuntimeMXBean mx = java.lang.management.ManagementFactory.getRuntimeMXBean();
		String[] mxNameTable = mx.getName().split("@"); //$NON-NLS-1$
		if (mxNameTable.length == 2) {
			org.slf4j.MDC.put("_systemPid", mxNameTable[0]);
		} else {
			org.slf4j.MDC.put("_systemPid", String.valueOf(java.lang.Thread.currentThread().getId()));
		}

		if (enableLogStash) {
			java.util.Properties properties_talendJobLog = new java.util.Properties();
			properties_talendJobLog.setProperty("root.logger", "audit");
			properties_talendJobLog.setProperty("encoding", "UTF-8");
			properties_talendJobLog.setProperty("application.name", "Talend Studio");
			properties_talendJobLog.setProperty("service.name", "Talend Studio Job");
			properties_talendJobLog.setProperty("instance.name", "Talend Studio Job Instance");
			properties_talendJobLog.setProperty("propagate.appender.exceptions", "none");
			properties_talendJobLog.setProperty("log.appender", "file");
			properties_talendJobLog.setProperty("appender.file.path", "audit.json");
			properties_talendJobLog.setProperty("appender.file.maxsize", "52428800");
			properties_talendJobLog.setProperty("appender.file.maxbackup", "20");
			properties_talendJobLog.setProperty("host", "false");

			System.getProperties().stringPropertyNames().stream().filter(it -> it.startsWith("audit.logger."))
					.forEach(key -> properties_talendJobLog.setProperty(key.substring("audit.logger.".length()),
							System.getProperty(key)));

			org.apache.logging.log4j.core.config.Configurator
					.setLevel(properties_talendJobLog.getProperty("root.logger"), org.apache.logging.log4j.Level.DEBUG);

			auditLogger_talendJobLog = org.talend.job.audit.JobEventAuditLoggerFactory
					.createJobAuditLogger(properties_talendJobLog);
		}

		if (clientHost == null) {
			clientHost = defaultClientHost;
		}

		if (pid == null || "0".equals(pid)) {
			pid = TalendString.getAsciiRandomString(6);
		}

		org.slf4j.MDC.put("_pid", pid);

		if (rootPid == null) {
			rootPid = pid;
		}

		org.slf4j.MDC.put("_rootPid", rootPid);

		if (fatherPid == null) {
			fatherPid = pid;
		} else {
			isChildJob = true;
		}
		org.slf4j.MDC.put("_fatherPid", fatherPid);

		if (portStats != null) {
			// portStats = -1; //for testing
			if (portStats < 0 || portStats > 65535) {
				// issue:10869, the portStats is invalid, so this client socket can't open
				System.err.println("The statistics socket port " + portStats + " is invalid.");
				execStat = false;
			}
		} else {
			execStat = false;
		}

		try {
			java.util.Dictionary<String, Object> jobProperties = null;
			if (inOSGi) {
				jobProperties = routines.system.BundleUtils.getJobProperties(jobName);

				if (jobProperties != null && jobProperties.get("context") != null) {
					contextStr = (String) jobProperties.get("context");
				}

				if (jobProperties != null && jobProperties.get("taskExecutionId") != null) {
					taskExecutionId = (String) jobProperties.get("taskExecutionId");
				}

				// extract ids from parent route
				if (null == taskExecutionId || taskExecutionId.isEmpty()) {
					for (String arg : args) {
						if (arg.startsWith("--context_param")
								&& (arg.contains("taskExecutionId") || arg.contains("jobExecutionId"))) {

							String keyValue = arg.replace("--context_param", "");
							String[] parts = keyValue.split("=");
							String[] cleanParts = java.util.Arrays.stream(parts).filter(s -> !s.isEmpty())
									.toArray(String[]::new);
							if (cleanParts.length == 2) {
								String key = cleanParts[0];
								String value = cleanParts[1];
								if ("taskExecutionId".equals(key.trim()) && null != value) {
									taskExecutionId = value.trim();
								} else if ("jobExecutionId".equals(key.trim()) && null != value) {
									jobExecutionId = value.trim();
								}
							}
						}
					}
				}
			}

			// first load default key-value pairs from application.properties
			if (isStandaloneMS) {
				context.putAll(this.getDefaultProperties());
			}
			// call job/subjob with an existing context, like: --context=production. if
			// without this parameter, there will use the default context instead.
			java.io.InputStream inContext = encrypt_and_decrypt.class.getClassLoader()
					.getResourceAsStream("dataprivacy/encrypt_and_decrypt_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = encrypt_and_decrypt.class.getClassLoader()
						.getResourceAsStream("config/contexts/" + contextStr + ".properties");
			}
			if (inContext != null) {
				try {
					// defaultProps is in order to keep the original context value
					if (context != null && context.isEmpty()) {
						defaultProps.load(inContext);
						if (inOSGi && jobProperties != null) {
							java.util.Enumeration<String> keys = jobProperties.keys();
							while (keys.hasMoreElements()) {
								String propKey = keys.nextElement();
								if (defaultProps.containsKey(propKey)) {
									defaultProps.put(propKey, (String) jobProperties.get(propKey));
								}
							}
						}
						context = new ContextProperties(defaultProps);
					}
					if (isStandaloneMS) {
						// override context key-value pairs if provided using --context=contextName
						defaultProps.load(inContext);
						context.putAll(defaultProps);
					}
				} finally {
					inContext.close();
				}
			} else if (!isDefaultContext) {
				// print info and job continue to run, for case: context_param is not empty.
				System.err.println("Could not find the context " + contextStr);
			}
			// override key-value pairs if provided via --config.location=file1.file2 OR
			// --config.additional-location=file1,file2
			if (isStandaloneMS) {
				context.putAll(this.getAdditionalProperties());
			}

			// override key-value pairs if provide via command line like
			// --key1=value1,--key2=value2
			if (!context_param.isEmpty()) {
				context.putAll(context_param);
				// set types for params from parentJobs
				for (Object key : context_param.keySet()) {
					String context_key = key.toString();
					String context_type = context_param.getContextType(context_key);
					context.setContextType(context_key, context_type);

				}
			}
			class ContextProcessing {
				private void processContext_0() {
					context.setContextType("CLIENTS_FILE_LOC", "id_String");
					if (context.getStringValue("CLIENTS_FILE_LOC") == null) {
						context.CLIENTS_FILE_LOC = null;
					} else {
						context.CLIENTS_FILE_LOC = (String) context.getProperty("CLIENTS_FILE_LOC");
					}
					context.setContextType("CyrptoFilePath", "id_String");
					if (context.getStringValue("CyrptoFilePath") == null) {
						context.CyrptoFilePath = null;
					} else {
						context.CyrptoFilePath = (String) context.getProperty("CyrptoFilePath");
					}
				}

				public void processAllContext() {
					processContext_0();
				}
			}

			new ContextProcessing().processAllContext();
		} catch (java.io.IOException ie) {
			System.err.println("Could not load context " + contextStr);
			ie.printStackTrace();
		}

		// get context value from parent directly
		if (parentContextMap != null && !parentContextMap.isEmpty()) {
			if (parentContextMap.containsKey("CLIENTS_FILE_LOC")) {
				context.CLIENTS_FILE_LOC = (String) parentContextMap.get("CLIENTS_FILE_LOC");
			}
			if (parentContextMap.containsKey("CyrptoFilePath")) {
				context.CyrptoFilePath = (String) parentContextMap.get("CyrptoFilePath");
			}
		}

		// Resume: init the resumeUtil
		resumeEntryMethodName = ResumeUtil.getResumeEntryMethodName(resuming_checkpoint_path);
		resumeUtil = new ResumeUtil(resuming_logs_dir_path, isChildJob, rootPid);
		resumeUtil.initCommonInfo(pid, rootPid, fatherPid, projectName, jobName, contextStr, jobVersion);

		List<String> parametersToEncrypt = new java.util.ArrayList<String>();
		// Resume: jobStart
		resumeUtil.addLog("JOB_STARTED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "",
				"", "", "", "", resumeUtil.convertToJsonText(context, ContextProperties.class, parametersToEncrypt));

		org.slf4j.MDC.put("_context", contextStr);
		log.info("TalendJob: 'encrypt_and_decrypt' - Started.");
		java.util.Optional.ofNullable(org.slf4j.MDC.getCopyOfContextMap()).ifPresent(mdcInfo::putAll);

		if (execStat) {
			try {
				runStat.openSocket(!isChildJob);
				runStat.setAllPID(rootPid, fatherPid, pid, jobName);
				runStat.startThreadStat(clientHost, portStats);
				runStat.updateStatOnJob(RunStat.JOBSTART, fatherNode);
			} catch (java.io.IOException ioException) {
				ioException.printStackTrace();
			}
		}

		java.util.concurrent.ConcurrentHashMap<Object, Object> concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<Object, Object>();
		globalMap.put("concurrentHashMap", concurrentHashMap);

		long startUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long endUsedMemory = 0;
		long end = 0;

		startTime = System.currentTimeMillis();

		this.globalResumeTicket = true;// to run tPreJob

		if (enableLogStash) {
			talendJobLog.addJobStartMessage();
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		this.globalResumeTicket = false;// to run others jobs

		try {
			errorCode = null;
			tFileInputDelimited_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tFileInputDelimited_1) {
			globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", -1);

			e_tFileInputDelimited_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println(
					(endUsedMemory - startUsedMemory) + " bytes memory increase when running : encrypt_and_decrypt");
		}
		if (enableLogStash) {
			talendJobLog.addJobEndMessage(startTime, end, status);
			try {
				talendJobLogProcess(globalMap);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		if (execStat) {
			runStat.updateStatOnJob(RunStat.JOBEND, fatherNode);
			runStat.stopThreadStat();
		}
		if (!inOSGi && isCBPClientPresent) {
			if (org.talend.metrics.CBPClient.getInstanceForCurrentVM() != null) {
				s("none");
				org.talend.metrics.CBPClient.getInstanceForCurrentVM().sendData();
			}
		}

		int returnCode = 0;

		if (errorCode == null) {
			returnCode = status != null && status.equals("failure") ? 1 : 0;
		} else {
			returnCode = errorCode.intValue();
		}
		resumeUtil.addLog("JOB_ENDED", "JOB:" + jobName, parent_part_launcher, Thread.currentThread().getId() + "", "",
				"" + returnCode, "", "", "");
		resumeUtil.flush();

		org.slf4j.MDC.remove("_subJobName");
		org.slf4j.MDC.remove("_subJobPid");
		org.slf4j.MDC.remove("_systemPid");
		log.info("TalendJob: 'encrypt_and_decrypt' - Finished - status: " + status + " returnCode: " + returnCode);

		return returnCode;

	}

	// only for OSGi env
	public void destroy() {
		// add CBP code for OSGI Executions
		if (null != taskExecutionId && !taskExecutionId.isEmpty()) {
			try {
				org.talend.metrics.DataReadTracker.setExecutionId(taskExecutionId, jobExecutionId, false);
				org.talend.metrics.DataReadTracker.sealCounter();
				org.talend.metrics.DataReadTracker.reset();
			} catch (Exception | NoClassDefFoundError e) {
				// ignore
			}
		}

	}

	private java.util.Map<String, Object> getSharedConnections4REST() {
		java.util.Map<String, Object> connections = new java.util.HashMap<String, Object>();

		return connections;
	}

	private void evalParam(String arg) {
		if (arg.startsWith("--resuming_logs_dir_path")) {
			resuming_logs_dir_path = arg.substring(25);
		} else if (arg.startsWith("--resuming_checkpoint_path")) {
			resuming_checkpoint_path = arg.substring(27);
		} else if (arg.startsWith("--parent_part_launcher")) {
			parent_part_launcher = arg.substring(23);
		} else if (arg.startsWith("--watch")) {
			watch = true;
		} else if (arg.startsWith("--stat_port=")) {
			String portStatsStr = arg.substring(12);
			if (portStatsStr != null && !portStatsStr.equals("null")) {
				portStats = Integer.parseInt(portStatsStr);
			}
		} else if (arg.startsWith("--trace_port=")) {
			portTraces = Integer.parseInt(arg.substring(13));
		} else if (arg.startsWith("--client_host=")) {
			clientHost = arg.substring(14);
		} else if (arg.startsWith("--context=")) {
			contextStr = arg.substring(10);
			isDefaultContext = false;
		} else if (arg.startsWith("--father_pid=")) {
			fatherPid = arg.substring(13);
		} else if (arg.startsWith("--root_pid=")) {
			rootPid = arg.substring(11);
		} else if (arg.startsWith("--father_node=")) {
			fatherNode = arg.substring(14);
		} else if (arg.startsWith("--pid=")) {
			pid = arg.substring(6);
		} else if (arg.startsWith("--context_type")) {
			String keyValue = arg.substring(15);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.setContextType(keyValue.substring(0, index),
							replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.setContextType(keyValue.substring(0, index), keyValue.substring(index + 1));
				}

			}

		} else if (arg.startsWith("--context_param")) {
			String keyValue = arg.substring(16);
			int index = -1;
			if (keyValue != null && (index = keyValue.indexOf('=')) > -1) {
				if (fatherPid == null) {
					context_param.put(keyValue.substring(0, index), replaceEscapeChars(keyValue.substring(index + 1)));
				} else { // the subjob won't escape the especial chars
					context_param.put(keyValue.substring(0, index), keyValue.substring(index + 1));
				}
			}
		} else if (arg.startsWith("--context_file")) {
			String keyValue = arg.substring(15);
			String filePath = new String(java.util.Base64.getDecoder().decode(keyValue));
			java.nio.file.Path contextFile = java.nio.file.Paths.get(filePath);
			try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(contextFile)) {
				String line;
				while ((line = reader.readLine()) != null) {
					int index = -1;
					if ((index = line.indexOf('=')) > -1) {
						if (line.startsWith("--context_param")) {
							if ("id_Password".equals(context_param.getContextType(line.substring(16, index)))) {
								context_param.put(line.substring(16, index),
										routines.system.PasswordEncryptUtil.decryptPassword(line.substring(index + 1)));
							} else {
								context_param.put(line.substring(16, index), line.substring(index + 1));
							}
						} else {// --context_type
							context_param.setContextType(line.substring(15, index), line.substring(index + 1));
						}
					}
				}
			} catch (java.io.IOException e) {
				System.err.println("Could not load the context file: " + filePath);
				e.printStackTrace();
			}
		} else if (arg.startsWith("--log4jLevel=")) {
			log4jLevel = arg.substring(13);
		} else if (arg.startsWith("--audit.enabled") && arg.contains("=")) {// for trunjob call
			final int equal = arg.indexOf('=');
			final String key = arg.substring("--".length(), equal);
			System.setProperty(key, arg.substring(equal + 1));
		}
	}

	private static final String NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY = "<TALEND_NULL>";

	private final String[][] escapeChars = { { "\\\\", "\\" }, { "\\n", "\n" }, { "\\'", "\'" }, { "\\r", "\r" },
			{ "\\f", "\f" }, { "\\b", "\b" }, { "\\t", "\t" } };

	private String replaceEscapeChars(String keyValue) {

		if (keyValue == null || ("").equals(keyValue.trim())) {
			return keyValue;
		}

		StringBuilder result = new StringBuilder();
		int currIndex = 0;
		while (currIndex < keyValue.length()) {
			int index = -1;
			// judege if the left string includes escape chars
			for (String[] strArray : escapeChars) {
				index = keyValue.indexOf(strArray[0], currIndex);
				if (index >= 0) {

					result.append(keyValue.substring(currIndex, index + strArray[0].length()).replace(strArray[0],
							strArray[1]));
					currIndex = index + strArray[0].length();
					break;
				}
			}
			// if the left string doesn't include escape chars, append the left into the
			// result
			if (index < 0) {
				result.append(keyValue.substring(currIndex));
				currIndex = currIndex + keyValue.length();
			}
		}

		return result.toString();
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getStatus() {
		return status;
	}

	ResumeUtil resumeUtil = null;
}
/************************************************************************************************
 * 244419 characters generated by Talend Cloud Data Management Platform on the
 * June 30, 2025 at 11:04:44 PM BST
 ************************************************************************************************/