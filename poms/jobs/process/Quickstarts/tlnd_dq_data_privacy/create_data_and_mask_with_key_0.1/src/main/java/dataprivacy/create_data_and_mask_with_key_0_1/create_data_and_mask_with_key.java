
package dataprivacy.create_data_and_mask_with_key_0_1;

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
 * Job: create_data_and_mask_with_key Purpose: <br>
 * Description: Generate a csv file. Mask the csv file using tDataMask. Use
 * tGenKey to create key for unmasking. <br>
 * 
 * @author Habushi, Ofer
 * @version 8.0.1.20250625_0954-patch
 * @status
 */
public class create_data_and_mask_with_key implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "create_data_and_mask_with_key.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(create_data_and_mask_with_key.class);

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

			if (DATA_FILE != null) {

				this.setProperty("DATA_FILE", DATA_FILE.toString());

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

		public String DATA_FILE;

		public String getDATA_FILE() {
			return this.DATA_FILE;
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
	private final String jobName = "create_data_and_mask_with_key";
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
			"_py4uwLJ8EeekK-npoh341w", "0.1");
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
				create_data_and_mask_with_key.this.exception = e;
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(create_data_and_mask_with_key.this,
									new Object[] { e, currentComponent, globalMap });
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

	public void tRowGenerator_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileOutputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tFileInputDelimited_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDataMasking_1_error(Exception exception, String errorComponent,
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

	public void tFileInputDelimited_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tGenKey_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_2_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tFileInputDelimited_2_onSubJobError(exception, errorComponent, globalMap);
	}

	public void talendJobLog_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		talendJobLog_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tRowGenerator_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tFileInputDelimited_1_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void tFileInputDelimited_2_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class row1Struct implements routines.system.IPersistableRow<row1Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];

		public String FirstName;

		public String getFirstName() {
			return this.FirstName;
		}

		public Boolean FirstNameIsNullable() {
			return true;
		}

		public Boolean FirstNameIsKey() {
			return false;
		}

		public Integer FirstNameLength() {
			return null;
		}

		public Integer FirstNamePrecision() {
			return null;
		}

		public String FirstNameDefault() {

			return null;

		}

		public String FirstNameComment() {

			return "";

		}

		public String FirstNamePattern() {

			return "";

		}

		public String FirstNameOriginalDbColumnName() {

			return "FirstName";

		}

		public String LastName;

		public String getLastName() {
			return this.LastName;
		}

		public Boolean LastNameIsNullable() {
			return true;
		}

		public Boolean LastNameIsKey() {
			return false;
		}

		public Integer LastNameLength() {
			return null;
		}

		public Integer LastNamePrecision() {
			return null;
		}

		public String LastNameDefault() {

			return null;

		}

		public String LastNameComment() {

			return "";

		}

		public String LastNamePattern() {

			return "";

		}

		public String LastNameOriginalDbColumnName() {

			return "LastName";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return null;
		}

		public Integer AddressPrecision() {
			return null;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

		}

		public String AddressPattern() {

			return "";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

		}

		public Integer SSN;

		public Integer getSSN() {
			return this.SSN;
		}

		public Boolean SSNIsNullable() {
			return true;
		}

		public Boolean SSNIsKey() {
			return false;
		}

		public Integer SSNLength() {
			return null;
		}

		public Integer SSNPrecision() {
			return null;
		}

		public String SSNDefault() {

			return null;

		}

		public String SSNComment() {

			return "";

		}

		public String SSNPattern() {

			return "";

		}

		public String SSNOriginalDbColumnName() {

			return "SSN";

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
			return null;
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

		public String email;

		public String getEmail() {
			return this.email;
		}

		public Boolean emailIsNullable() {
			return true;
		}

		public Boolean emailIsKey() {
			return false;
		}

		public Integer emailLength() {
			return null;
		}

		public Integer emailPrecision() {
			return null;
		}

		public String emailDefault() {

			return "";

		}

		public String emailComment() {

			return "";

		}

		public String emailPattern() {

			return "";

		}

		public String emailOriginalDbColumnName() {

			return "email";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readInteger(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readInteger(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// Integer

				writeInteger(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// Integer

				writeInteger(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",Address=" + Address);
			sb.append(",SSN=" + String.valueOf(SSN));
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",email=" + email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (FirstName == null) {
				sb.append("<null>");
			} else {
				sb.append(FirstName);
			}

			sb.append("|");

			if (LastName == null) {
				sb.append("<null>");
			} else {
				sb.append(LastName);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
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

	public void tRowGenerator_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tRowGenerator_1", "ZE4yy6_");

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

				/**
				 * [tFileOutputDelimited_1 begin ] start
				 */

				sh("tFileOutputDelimited_1");

				s(currentComponent = "tFileOutputDelimited_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tFileOutputDelimited_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileOutputDelimited_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileOutputDelimited_1 = new StringBuilder();
							log4jParamters_tFileOutputDelimited_1.append("Parameters:");
							log4jParamters_tFileOutputDelimited_1.append("USESTREAM" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FILENAME" + " = " + "context.DATA_FILE");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("APPEND" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("INCLUDEHEADER" + " = " + "true");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("COMPRESS" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ADVANCED_SEPARATOR" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("CREATE" + " = " + "true");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("SPLIT" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FLUSHONROW" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ROW_MODE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("ENCODING" + " = " + "\"ISO-8859-15\"");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("DELETE_EMPTYFILE" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							log4jParamters_tFileOutputDelimited_1.append("FILE_EXIST_EXCEPTION" + " = " + "false");
							log4jParamters_tFileOutputDelimited_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileOutputDelimited_1 - " + (log4jParamters_tFileOutputDelimited_1));
						}
					}
					new BytesLimit65535_tFileOutputDelimited_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileOutputDelimited_1", "tFileOutputDelimited_1", "tFileOutputDelimited");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				String fileName_tFileOutputDelimited_1 = "";
				fileName_tFileOutputDelimited_1 = (new java.io.File(context.DATA_FILE)).getAbsolutePath().replace("\\",
						"/");
				String fullName_tFileOutputDelimited_1 = null;
				String extension_tFileOutputDelimited_1 = null;
				String directory_tFileOutputDelimited_1 = null;
				if ((fileName_tFileOutputDelimited_1.indexOf("/") != -1)) {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") < fileName_tFileOutputDelimited_1
							.lastIndexOf("/")) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1.substring(0,
								fileName_tFileOutputDelimited_1.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1.lastIndexOf("."));
					}
					directory_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1.substring(0,
							fileName_tFileOutputDelimited_1.lastIndexOf("/"));
				} else {
					if (fileName_tFileOutputDelimited_1.lastIndexOf(".") != -1) {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1.substring(0,
								fileName_tFileOutputDelimited_1.lastIndexOf("."));
						extension_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1
								.substring(fileName_tFileOutputDelimited_1.lastIndexOf("."));
					} else {
						fullName_tFileOutputDelimited_1 = fileName_tFileOutputDelimited_1;
						extension_tFileOutputDelimited_1 = "";
					}
					directory_tFileOutputDelimited_1 = "";
				}
				boolean isFileGenerated_tFileOutputDelimited_1 = true;
				java.io.File filetFileOutputDelimited_1 = new java.io.File(fileName_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME", fileName_tFileOutputDelimited_1);
				int nb_line_tFileOutputDelimited_1 = 0;
				int splitedFileNo_tFileOutputDelimited_1 = 0;
				int currentRow_tFileOutputDelimited_1 = 0;

				final String OUT_DELIM_tFileOutputDelimited_1 = /** Start field tFileOutputDelimited_1:FIELDSEPARATOR */
						";"/** End field tFileOutputDelimited_1:FIELDSEPARATOR */
				;

				final String OUT_DELIM_ROWSEP_tFileOutputDelimited_1 = /**
																		 * Start field
																		 * tFileOutputDelimited_1:ROWSEPARATOR
																		 */
						"\n"/** End field tFileOutputDelimited_1:ROWSEPARATOR */
				;

				// create directory only if not exists
				if (directory_tFileOutputDelimited_1 != null && directory_tFileOutputDelimited_1.trim().length() != 0) {
					java.io.File dir_tFileOutputDelimited_1 = new java.io.File(directory_tFileOutputDelimited_1);
					if (!dir_tFileOutputDelimited_1.exists()) {
						log.info("tFileOutputDelimited_1 - Creating directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath() + "'.");
						dir_tFileOutputDelimited_1.mkdirs();
						log.info("tFileOutputDelimited_1 - The directory '"
								+ dir_tFileOutputDelimited_1.getCanonicalPath() + "' has been created successfully.");
					}
				}

				// routines.system.Row
				java.io.Writer outtFileOutputDelimited_1 = null;

				java.io.File fileToDelete_tFileOutputDelimited_1 = new java.io.File(fileName_tFileOutputDelimited_1);
				if (fileToDelete_tFileOutputDelimited_1.exists()) {
					fileToDelete_tFileOutputDelimited_1.delete();
				}
				outtFileOutputDelimited_1 = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
						new java.io.FileOutputStream(fileName_tFileOutputDelimited_1, false), "ISO-8859-15"));
				resourceMap.put("out_tFileOutputDelimited_1", outtFileOutputDelimited_1);
				if (filetFileOutputDelimited_1.length() == 0) {
					outtFileOutputDelimited_1.write("FirstName");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("LastName");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("Address");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("SSN");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("CreditCard");
					outtFileOutputDelimited_1.write(OUT_DELIM_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.write("email");
					outtFileOutputDelimited_1.write(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);
					outtFileOutputDelimited_1.flush();
				}

				resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

				/**
				 * [tFileOutputDelimited_1 begin ] stop
				 */

				/**
				 * [tRowGenerator_1 begin ] start
				 */

				sh("tRowGenerator_1");

				s(currentComponent = "tRowGenerator_1");

				int tos_count_tRowGenerator_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tRowGenerator_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tRowGenerator_1 = new StringBuilder();
							log4jParamters_tRowGenerator_1.append("Parameters:");
							if (log.isDebugEnabled())
								log.debug("tRowGenerator_1 - " + (log4jParamters_tRowGenerator_1));
						}
					}
					new BytesLimit65535_tRowGenerator_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tRowGenerator_1", "tRowGenerator_1", "tRowGenerator");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				int nb_line_tRowGenerator_1 = 0;
				int nb_max_row_tRowGenerator_1 = 10;

				class tRowGenerator_1Randomizer {
					public String getRandomFirstName() {

						return TalendDataGenerator.getFirstName();

					}

					public String getRandomLastName() {

						return TalendDataGenerator.getLastName();

					}

					public String getRandomAddress() {

						return TalendDataGenerator.getUsStreet();

					}

					public Integer getRandomSSN() {

						return Numeric.random(111111111, 999999999);

					}

					public String getRandomCreditCard() {

						String[] CreditCardTable = new String[] { "1111111111111111", "2222222222222222",
								"3333333333333333", "4444444444444444", "5555555555555555" };
						java.util.Random randomtRowGenerator_1 = new java.util.Random();
						return CreditCardTable[randomtRowGenerator_1.nextInt(CreditCardTable.length)];

					}

					public String getRandomemail() {

						String[] emailTable = new String[] { "thoff@talend.com", "john@talend.com", "fred@talend.com",
								"wilma@talend.com" };
						java.util.Random randomtRowGenerator_1 = new java.util.Random();
						return emailTable[randomtRowGenerator_1.nextInt(emailTable.length)];

					}
				}
				tRowGenerator_1Randomizer randtRowGenerator_1 = new tRowGenerator_1Randomizer();

				log.info("tRowGenerator_1 - Generating records.");
				for (int itRowGenerator_1 = 0; itRowGenerator_1 < nb_max_row_tRowGenerator_1; itRowGenerator_1++) {
					row1.FirstName = randtRowGenerator_1.getRandomFirstName();
					row1.LastName = randtRowGenerator_1.getRandomLastName();
					row1.Address = randtRowGenerator_1.getRandomAddress();
					row1.SSN = randtRowGenerator_1.getRandomSSN();
					row1.CreditCard = randtRowGenerator_1.getRandomCreditCard();
					row1.email = randtRowGenerator_1.getRandomemail();
					nb_line_tRowGenerator_1++;

					log.debug("tRowGenerator_1 - Retrieving the record " + nb_line_tRowGenerator_1 + ".");

					/**
					 * [tRowGenerator_1 begin ] stop
					 */

					/**
					 * [tRowGenerator_1 main ] start
					 */

					s(currentComponent = "tRowGenerator_1");

					tos_count_tRowGenerator_1++;

					/**
					 * [tRowGenerator_1 main ] stop
					 */

					/**
					 * [tRowGenerator_1 process_data_begin ] start
					 */

					s(currentComponent = "tRowGenerator_1");

					/**
					 * [tRowGenerator_1 process_data_begin ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 main ] start
					 */

					s(currentComponent = "tFileOutputDelimited_1");

					if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

							, "row1", "tRowGenerator_1", "tRowGenerator_1", "tRowGenerator", "tFileOutputDelimited_1",
							"tFileOutputDelimited_1", "tFileOutputDelimited"

					)) {
						talendJobLogProcess(globalMap);
					}

					if (log.isTraceEnabled()) {
						log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
					}

					StringBuilder sb_tFileOutputDelimited_1 = new StringBuilder();
					if (row1.FirstName != null) {
						sb_tFileOutputDelimited_1.append(row1.FirstName);
					}
					sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.LastName != null) {
						sb_tFileOutputDelimited_1.append(row1.LastName);
					}
					sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.Address != null) {
						sb_tFileOutputDelimited_1.append(row1.Address);
					}
					sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.SSN != null) {
						sb_tFileOutputDelimited_1.append(row1.SSN);
					}
					sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.CreditCard != null) {
						sb_tFileOutputDelimited_1.append(row1.CreditCard);
					}
					sb_tFileOutputDelimited_1.append(OUT_DELIM_tFileOutputDelimited_1);
					if (row1.email != null) {
						sb_tFileOutputDelimited_1.append(row1.email);
					}
					sb_tFileOutputDelimited_1.append(OUT_DELIM_ROWSEP_tFileOutputDelimited_1);

					nb_line_tFileOutputDelimited_1++;
					resourceMap.put("nb_line_tFileOutputDelimited_1", nb_line_tFileOutputDelimited_1);

					outtFileOutputDelimited_1.write(sb_tFileOutputDelimited_1.toString());
					log.debug("tFileOutputDelimited_1 - Writing the record " + nb_line_tFileOutputDelimited_1 + ".");

					tos_count_tFileOutputDelimited_1++;

					/**
					 * [tFileOutputDelimited_1 main ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 process_data_begin ] start
					 */

					s(currentComponent = "tFileOutputDelimited_1");

					/**
					 * [tFileOutputDelimited_1 process_data_begin ] stop
					 */

					/**
					 * [tFileOutputDelimited_1 process_data_end ] start
					 */

					s(currentComponent = "tFileOutputDelimited_1");

					/**
					 * [tFileOutputDelimited_1 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_1 process_data_end ] start
					 */

					s(currentComponent = "tRowGenerator_1");

					/**
					 * [tRowGenerator_1 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_1 end ] start
					 */

					s(currentComponent = "tRowGenerator_1");

				}
				globalMap.put("tRowGenerator_1_NB_LINE", nb_line_tRowGenerator_1);
				log.info("tRowGenerator_1 - Generated records count:" + nb_line_tRowGenerator_1 + " .");

				if (log.isDebugEnabled())
					log.debug("tRowGenerator_1 - " + ("Done."));

				ok_Hash.put("tRowGenerator_1", true);
				end_Hash.put("tRowGenerator_1", System.currentTimeMillis());

				/**
				 * [tRowGenerator_1 end ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 end ] start
				 */

				s(currentComponent = "tFileOutputDelimited_1");

				if (outtFileOutputDelimited_1 != null) {
					outtFileOutputDelimited_1.flush();
					outtFileOutputDelimited_1.close();
				}

				globalMap.put("tFileOutputDelimited_1_NB_LINE", nb_line_tFileOutputDelimited_1);
				globalMap.put("tFileOutputDelimited_1_FILE_NAME", fileName_tFileOutputDelimited_1);

				resourceMap.put("finish_tFileOutputDelimited_1", true);

				log.debug("tFileOutputDelimited_1 - Written records count: " + nb_line_tFileOutputDelimited_1 + " .");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tRowGenerator_1", "tRowGenerator_1", "tRowGenerator", "tFileOutputDelimited_1",
						"tFileOutputDelimited_1", "tFileOutputDelimited", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tFileOutputDelimited_1 - " + ("Done."));

				ok_Hash.put("tFileOutputDelimited_1", true);
				end_Hash.put("tFileOutputDelimited_1", System.currentTimeMillis());

				/**
				 * [tFileOutputDelimited_1 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tRowGenerator_1:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk1", 0, "ok");
			}

			tFileInputDelimited_1Process(globalMap);

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
				 * [tRowGenerator_1 finally ] start
				 */

				s(currentComponent = "tRowGenerator_1");

				/**
				 * [tRowGenerator_1 finally ] stop
				 */

				/**
				 * [tFileOutputDelimited_1 finally ] start
				 */

				s(currentComponent = "tFileOutputDelimited_1");

				if (resourceMap.get("finish_tFileOutputDelimited_1") == null) {

					java.io.Writer outtFileOutputDelimited_1 = (java.io.Writer) resourceMap
							.get("out_tFileOutputDelimited_1");
					if (outtFileOutputDelimited_1 != null) {
						outtFileOutputDelimited_1.flush();
						outtFileOutputDelimited_1.close();
					}

				}

				/**
				 * [tFileOutputDelimited_1 finally ] stop
				 */

			} catch (java.lang.Exception e) {
				// ignore
			} catch (java.lang.Error error) {
				// ignore
			}
			resourceMap = null;
		}

		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 1);
	}

	public static class row2Struct implements routines.system.IPersistableRow<row2Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];

		public String FirstName;

		public String getFirstName() {
			return this.FirstName;
		}

		public Boolean FirstNameIsNullable() {
			return true;
		}

		public Boolean FirstNameIsKey() {
			return false;
		}

		public Integer FirstNameLength() {
			return 7;
		}

		public Integer FirstNamePrecision() {
			return 0;
		}

		public String FirstNameDefault() {

			return null;

		}

		public String FirstNameComment() {

			return "";

		}

		public String FirstNamePattern() {

			return "dd-MM-yyyy";

		}

		public String FirstNameOriginalDbColumnName() {

			return "FirstName";

		}

		public String LastName;

		public String getLastName() {
			return this.LastName;
		}

		public Boolean LastNameIsNullable() {
			return true;
		}

		public Boolean LastNameIsKey() {
			return false;
		}

		public Integer LastNameLength() {
			return 9;
		}

		public Integer LastNamePrecision() {
			return 0;
		}

		public String LastNameDefault() {

			return null;

		}

		public String LastNameComment() {

			return "";

		}

		public String LastNamePattern() {

			return "dd-MM-yyyy";

		}

		public String LastNameOriginalDbColumnName() {

			return "LastName";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 21;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

		}

		public String AddressPattern() {

			return "dd-MM-yyyy";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

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
			return null;
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
			return null;
		}

		public Integer CreditCardPrecision() {
			return 0;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "dd-MM-yyyy";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		public Boolean emailIsNullable() {
			return true;
		}

		public Boolean emailIsKey() {
			return false;
		}

		public Integer emailLength() {
			return 16;
		}

		public Integer emailPrecision() {
			return 0;
		}

		public String emailDefault() {

			return null;

		}

		public String emailComment() {

			return "";

		}

		public String emailPattern() {

			return "dd-MM-yyyy";

		}

		public String emailOriginalDbColumnName() {

			return "email";

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

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

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

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

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

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

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

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

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
			sb.append("FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",Address=" + Address);
			sb.append(",SSN=" + SSN);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",email=" + email);
			sb.append(",ORIGINAL_MARK=" + String.valueOf(ORIGINAL_MARK));
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (FirstName == null) {
				sb.append("<null>");
			} else {
				sb.append(FirstName);
			}

			sb.append("|");

			if (LastName == null) {
				sb.append("<null>");
			} else {
				sb.append(LastName);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
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

	public static class row3Struct implements routines.system.IPersistableRow<row3Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];

		public String FirstName;

		public String getFirstName() {
			return this.FirstName;
		}

		public Boolean FirstNameIsNullable() {
			return true;
		}

		public Boolean FirstNameIsKey() {
			return false;
		}

		public Integer FirstNameLength() {
			return 7;
		}

		public Integer FirstNamePrecision() {
			return 0;
		}

		public String FirstNameDefault() {

			return null;

		}

		public String FirstNameComment() {

			return "";

		}

		public String FirstNamePattern() {

			return "dd-MM-yyyy";

		}

		public String FirstNameOriginalDbColumnName() {

			return "FirstName";

		}

		public String LastName;

		public String getLastName() {
			return this.LastName;
		}

		public Boolean LastNameIsNullable() {
			return true;
		}

		public Boolean LastNameIsKey() {
			return false;
		}

		public Integer LastNameLength() {
			return 9;
		}

		public Integer LastNamePrecision() {
			return 0;
		}

		public String LastNameDefault() {

			return null;

		}

		public String LastNameComment() {

			return "";

		}

		public String LastNamePattern() {

			return "dd-MM-yyyy";

		}

		public String LastNameOriginalDbColumnName() {

			return "LastName";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 21;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

		}

		public String AddressPattern() {

			return "dd-MM-yyyy";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

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
			return null;
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
			return null;
		}

		public Integer CreditCardPrecision() {
			return 0;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "dd-MM-yyyy";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		public Boolean emailIsNullable() {
			return true;
		}

		public Boolean emailIsKey() {
			return false;
		}

		public Integer emailLength() {
			return 16;
		}

		public Integer emailPrecision() {
			return 0;
		}

		public String emailDefault() {

			return null;

		}

		public String emailComment() {

			return "";

		}

		public String emailPattern() {

			return "dd-MM-yyyy";

		}

		public String emailOriginalDbColumnName() {

			return "email";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",Address=" + Address);
			sb.append(",SSN=" + SSN);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",email=" + email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (FirstName == null) {
				sb.append("<null>");
			} else {
				sb.append(FirstName);
			}

			sb.append("|");

			if (LastName == null) {
				sb.append("<null>");
			} else {
				sb.append(LastName);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
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

	public void tFileInputDelimited_1Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tFileInputDelimited_1_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tFileInputDelimited_1", "7ayxOp_");

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

				row3Struct row3 = new row3Struct();
				row2Struct row2 = new row2Struct();

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

					int[] colLengths = new int[7];

					public void addRow(String[] row) {

						for (int i = 0; i < 7; i++) {
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
						for (k = 0; k < (totals + 6 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 6 - name.length() - k; i++) {
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

						// last column
						for (int i = 0; i < colLengths[6] - fillChars[1].length() + 1; i++) {
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
				util_tLogRow_1.addRow(new String[] { "FirstName", "LastName", "Address", "SSN", "CreditCard", "email",
						"ORIGINAL_MARK", });
				StringBuilder strBuffer_tLogRow_1 = null;
				int nb_line_tLogRow_1 = 0;
///////////////////////    			

				/**
				 * [tLogRow_1 begin ] stop
				 */

				/**
				 * [tDataMasking_1 begin ] start
				 */

				sh("tDataMasking_1");

				s(currentComponent = "tDataMasking_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row3");

				int tos_count_tDataMasking_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDataMasking_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDataMasking_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDataMasking_1 = new StringBuilder();
							log4jParamters_tDataMasking_1.append("Parameters:");
							log4jParamters_tDataMasking_1.append("MODIF_TABLE" + " = " + "[{EXTRA_PARAMETER=" + ("\"\"")
									+ ", CATEGORY=" + ("CHARACTER_HANDLING") + ", METHOD=" + ("RANDOM")
									+ ", INPUT_COLUMN=" + ("FirstName") + ", FUNCTION=" + ("REPLACE_ALL")
									+ ", ALPHABET=" + ("BEST_GUESS") + ", KEEP_FORMAT=" + ("false")
									+ "}, {EXTRA_PARAMETER=" + ("\"4\"") + ", CATEGORY=" + ("CHARACTER_HANDLING")
									+ ", METHOD=" + ("RANDOM") + ", INPUT_COLUMN=" + ("LastName") + ", FUNCTION="
									+ ("REPLACE_LAST_CHARS") + ", ALPHABET=" + ("BEST_GUESS") + ", KEEP_FORMAT="
									+ ("false") + "}, {EXTRA_PARAMETER=" + ("\"\"") + ", CATEGORY="
									+ ("ADDRESS_MASKING") + ", METHOD=" + ("EMPTY") + ", INPUT_COLUMN=" + ("Address")
									+ ", FUNCTION=" + ("MASK_ADDRESS") + ", ALPHABET=" + ("BEST_GUESS")
									+ ", KEEP_FORMAT=" + ("false") + "}, {EXTRA_PARAMETER=" + ("\"\"") + ", CATEGORY="
									+ ("SSN_MASKING") + ", METHOD=" + ("BASIC") + ", INPUT_COLUMN=" + ("SSN")
									+ ", FUNCTION=" + ("GENERATE_UNIQUE_SSN_US") + ", ALPHABET=" + ("BEST_GUESS")
									+ ", KEEP_FORMAT=" + ("false") + "}, {EXTRA_PARAMETER=" + ("\"5,10\"")
									+ ", CATEGORY=" + ("CHARACTER_HANDLING") + ", METHOD=" + ("RANDOM")
									+ ", INPUT_COLUMN=" + ("CreditCard") + ", FUNCTION=" + ("BETWEEN_INDEXES_REPLACE")
									+ ", ALPHABET=" + ("BEST_GUESS") + ", KEEP_FORMAT=" + ("false")
									+ "}, {EXTRA_PARAMETER=" + ("\"x\"") + ", CATEGORY=" + ("EMAIL_MASKING")
									+ ", METHOD=" + ("MASK_BY_CHARACTER") + ", INPUT_COLUMN=" + ("email")
									+ ", FUNCTION=" + ("MASK_FULL_EMAIL_DOMAIN") + ", ALPHABET=" + ("BEST_GUESS")
									+ ", KEEP_FORMAT=" + ("false") + "}]");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("PASSWORD" + " = "
									+ String.valueOf(
											"enc:routine.encryption.key.v1:UJy7xYw6/Vu/yh5WhiOuhO6XEyKXgZ81lsLtBQ==")
											.substring(0, 4)
									+ "...");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("USE_TWEAK" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("ALGO_VERSION" + " = " + "1");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("RANDOM_SEED" + " = " + "12345678");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("ENCODING" + " = " + "\"\"");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("OUTPUT_ORIGINAL" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("KEEP_NULL" + " = " + "true");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("KEEP_EMPTY" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("SEPERATE_OUTPUT" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tDataMasking_1 - " + (log4jParamters_tDataMasking_1));
						}
					}
					new BytesLimit65535_tDataMasking_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tDataMasking_1", "tDataMasking_1", "tDataMasking");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				final org.talend.dataquality.datamasking.FunctionFactory fun_fact_tDataMasking_1 = new org.talend.dataquality.datamasking.FunctionFactory();
				final org.talend.dataquality.datamasking.TypeTester typeTester_tDataMasking_1 = new org.talend.dataquality.datamasking.TypeTester();
				final java.util.Random rnd_tDataMasking_1 = new java.util.Random(Long.valueOf("" + 12345678l));

				String value_tDataMasking_11 = null;
				int type_tDataMasking_11 = typeTester_tDataMasking_1.getType(value_tDataMasking_11);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_11 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("REPLACE_ALL", type_tDataMasking_11, "RANDOM");
				fun_tDataMasking_11.setMaskingMode(org.talend.dataquality.datamasking.FunctionMode.valueOf("RANDOM"));

				fun_tDataMasking_11.setCharSetName("");
				fun_tDataMasking_11.parse("", true, rnd_tDataMasking_1);

				fun_tDataMasking_11.setSeed("" + 12345678l);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_11)) {
					fun_tDataMasking_11.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_11.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils
						.isValidEnum(org.talend.dataquality.datamasking.FormatPreservingMethod.class, "RANDOM")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_11)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_11)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_11)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_11))) {
					fun_tDataMasking_11.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("RANDOM"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:zIDs8x+Xt1oZpQNHQl96vb3bzzE5YNXqdJmpiQ=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_11.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_11.setKeepFormat(false);
				fun_tDataMasking_11.setKeepEmpty(false);
				fun_tDataMasking_11.setUseTweak(false);
				String value_tDataMasking_12 = null;
				int type_tDataMasking_12 = typeTester_tDataMasking_1.getType(value_tDataMasking_12);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_12 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("REPLACE_LAST_CHARS", type_tDataMasking_12, "RANDOM");
				fun_tDataMasking_12.setMaskingMode(org.talend.dataquality.datamasking.FunctionMode.valueOf("RANDOM"));

				fun_tDataMasking_12.setCharSetName("");
				fun_tDataMasking_12.parse("4", true, rnd_tDataMasking_1);

				fun_tDataMasking_12.setSeed("" + 12345678l);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_12)) {
					fun_tDataMasking_12.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_12.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils
						.isValidEnum(org.talend.dataquality.datamasking.FormatPreservingMethod.class, "RANDOM")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_12)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_12)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_12)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_12))) {
					fun_tDataMasking_12.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("RANDOM"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:zIDs8x+Xt1oZpQNHQl96vb3bzzE5YNXqdJmpiQ=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_12.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_12.setKeepFormat(false);
				fun_tDataMasking_12.setKeepEmpty(false);
				fun_tDataMasking_12.setUseTweak(false);
				String value_tDataMasking_13 = null;
				int type_tDataMasking_13 = typeTester_tDataMasking_1.getType(value_tDataMasking_13);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_13 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("MASK_ADDRESS", type_tDataMasking_13, "EMPTY");

				fun_tDataMasking_13.setCharSetName("");
				fun_tDataMasking_13.parse("", true, rnd_tDataMasking_1);

				fun_tDataMasking_13.setSeed("" + 12345678l);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_13)) {
					fun_tDataMasking_13.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_13.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils
						.isValidEnum(org.talend.dataquality.datamasking.FormatPreservingMethod.class, "EMPTY")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_13)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_13)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_13)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_13))) {
					fun_tDataMasking_13.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("EMPTY"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:zIDs8x+Xt1oZpQNHQl96vb3bzzE5YNXqdJmpiQ=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_13.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_13.setKeepFormat(false);
				fun_tDataMasking_13.setKeepEmpty(false);
				fun_tDataMasking_13.setUseTweak(false);
				String value_tDataMasking_14 = null;
				int type_tDataMasking_14 = typeTester_tDataMasking_1.getType(value_tDataMasking_14);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_14 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("GENERATE_UNIQUE_SSN_US", type_tDataMasking_14, "BASIC");
				fun_tDataMasking_14
						.setMaskingMode(org.talend.dataquality.datamasking.FunctionMode.valueOf("BIJECTIVE"));

				fun_tDataMasking_14.setCharSetName("");
				fun_tDataMasking_14.parse("", true, rnd_tDataMasking_1);

				fun_tDataMasking_14.setSeed("" + 12345678l);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_14)) {
					fun_tDataMasking_14.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_14.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils
						.isValidEnum(org.talend.dataquality.datamasking.FormatPreservingMethod.class, "BASIC")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_14)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_14)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_14)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_14))) {
					fun_tDataMasking_14.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("BASIC"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:zIDs8x+Xt1oZpQNHQl96vb3bzzE5YNXqdJmpiQ=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_14.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_14.setKeepFormat(false);
				fun_tDataMasking_14.setKeepEmpty(false);
				fun_tDataMasking_14.setUseTweak(false);
				String value_tDataMasking_15 = null;
				int type_tDataMasking_15 = typeTester_tDataMasking_1.getType(value_tDataMasking_15);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_15 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("BETWEEN_INDEXES_REPLACE", type_tDataMasking_15, "RANDOM");
				fun_tDataMasking_15.setMaskingMode(org.talend.dataquality.datamasking.FunctionMode.valueOf("RANDOM"));

				fun_tDataMasking_15.setCharSetName("");
				fun_tDataMasking_15.parse("5,10", true, rnd_tDataMasking_1);

				fun_tDataMasking_15.setSeed("" + 12345678l);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_15)) {
					fun_tDataMasking_15.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_15.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils
						.isValidEnum(org.talend.dataquality.datamasking.FormatPreservingMethod.class, "RANDOM")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_15)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_15)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_15)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_15))) {
					fun_tDataMasking_15.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("RANDOM"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:zIDs8x+Xt1oZpQNHQl96vb3bzzE5YNXqdJmpiQ=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_15.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_15.setKeepFormat(false);
				fun_tDataMasking_15.setKeepEmpty(false);
				fun_tDataMasking_15.setUseTweak(false);
				String value_tDataMasking_16 = null;
				int type_tDataMasking_16 = typeTester_tDataMasking_1.getType(value_tDataMasking_16);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_16 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("MASK_FULL_EMAIL_DOMAIN", type_tDataMasking_16, "MASK_BY_CHARACTER");
				fun_tDataMasking_16
						.setMaskingMode(org.talend.dataquality.datamasking.FunctionMode.valueOf("MASK_BY_CHARACTER"));

				fun_tDataMasking_16.setCharSetName("");
				fun_tDataMasking_16.parse("x", true, rnd_tDataMasking_1);

				fun_tDataMasking_16.setSeed("" + 12345678l);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_16)) {
					fun_tDataMasking_16.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_16.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils.isValidEnum(
						org.talend.dataquality.datamasking.FormatPreservingMethod.class, "MASK_BY_CHARACTER")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_16)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_16)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_16)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_16))) {
					fun_tDataMasking_16.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("MASK_BY_CHARACTER"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:zIDs8x+Xt1oZpQNHQl96vb3bzzE5YNXqdJmpiQ=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_16.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_16.setKeepFormat(false);
				fun_tDataMasking_16.setKeepEmpty(false);
				fun_tDataMasking_16.setUseTweak(false);

				class errorMessageStruct_tDataMasking_1Struct {

					public String ERROR_MESSAGE;

					public String getERROR_MESSAGE() {
						return this.ERROR_MESSAGE;
					}

				}
				final errorMessageStruct_tDataMasking_1Struct errorMessage_tDataMasking_1 = new errorMessageStruct_tDataMasking_1Struct();
				org.talend.dataquality.datamasking.DataMasker<row3Struct, row2Struct> duplicator_tDataMasking_1 = new org.talend.dataquality.datamasking.DataMasker<row3Struct, row2Struct>() {
					@Override
					protected row2Struct generateOutput(row3Struct originalStruct, boolean isOriginal) {
						row2Struct tmpStruct = new row2Struct();

						tmpStruct.FirstName = originalStruct.FirstName;
						tmpStruct.LastName = originalStruct.LastName;
						tmpStruct.Address = originalStruct.Address;
						tmpStruct.SSN = originalStruct.SSN;
						tmpStruct.CreditCard = originalStruct.CreditCard;
						tmpStruct.email = originalStruct.email;

						if (isOriginal) {
							tmpStruct.ORIGINAL_MARK = true;
						} else {
							modifyOutput(tmpStruct);
							tmpStruct.ORIGINAL_MARK = false;
						}

						return tmpStruct;
					}

					private void modifyOutput(row2Struct row2) {
						Object tmpValue_tDataMasking_1 = null;
						int nbColumns = 0;

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_11 = false;
						boolean isInputEmpty_tDataMasking_11 = false;
						boolean outputNullInMain_tDataMasking_11 = false;
						if (row2.FirstName == null) {
							isInputNull_tDataMasking_11 = true;
						} else if ("".equals(row2.FirstName.toString().trim())) {
							isInputEmpty_tDataMasking_11 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_11) || (false && isInputEmpty_tDataMasking_11)) {
							tmpValue_tDataMasking_1 = row2.FirstName;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_11.generateMaskedRow(row2.FirstName);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_11)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_11) && (false || false))) {
								/*
								 * when doesn't check "send invalid data to invalid flow", then all data in the
								 * main flow, so the invalid values should show null
								 */
								row2.FirstName = null;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row2.FirstName = null;
								}

							} else {

								row2.FirstName = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_12 = false;
						boolean isInputEmpty_tDataMasking_12 = false;
						boolean outputNullInMain_tDataMasking_12 = false;
						if (row2.LastName == null) {
							isInputNull_tDataMasking_12 = true;
						} else if ("".equals(row2.LastName.toString().trim())) {
							isInputEmpty_tDataMasking_12 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_12) || (false && isInputEmpty_tDataMasking_12)) {
							tmpValue_tDataMasking_1 = row2.LastName;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_12.generateMaskedRow(row2.LastName);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_12)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_12) && (false || false))) {
								/*
								 * when doesn't check "send invalid data to invalid flow", then all data in the
								 * main flow, so the invalid values should show null
								 */
								row2.LastName = null;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row2.LastName = null;
								}

							} else {

								row2.LastName = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_13 = false;
						boolean isInputEmpty_tDataMasking_13 = false;
						boolean outputNullInMain_tDataMasking_13 = false;
						if (row2.Address == null) {
							isInputNull_tDataMasking_13 = true;
						} else if ("".equals(row2.Address.toString().trim())) {
							isInputEmpty_tDataMasking_13 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_13) || (false && isInputEmpty_tDataMasking_13)) {
							tmpValue_tDataMasking_1 = row2.Address;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_13.generateMaskedRow(row2.Address);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_13)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_13) && (false || false))) {
								/*
								 * when doesn't check "send invalid data to invalid flow", then all data in the
								 * main flow, so the invalid values should show null
								 */
								row2.Address = null;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row2.Address = null;
								}

							} else {

								row2.Address = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_14 = false;
						boolean isInputEmpty_tDataMasking_14 = false;
						boolean outputNullInMain_tDataMasking_14 = false;
						if (row2.SSN == null) {
							isInputNull_tDataMasking_14 = true;
						} else if ("".equals(row2.SSN.toString().trim())) {
							isInputEmpty_tDataMasking_14 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_14) || (false && isInputEmpty_tDataMasking_14)) {
							tmpValue_tDataMasking_1 = row2.SSN;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_14.generateMaskedRow(row2.SSN);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_14)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_14) && (false || false))) {
								/*
								 * when doesn't check "send invalid data to invalid flow", then all data in the
								 * main flow, so the invalid values should show null
								 */
								row2.SSN = null;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row2.SSN = null;
								}

							} else {

								row2.SSN = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_15 = false;
						boolean isInputEmpty_tDataMasking_15 = false;
						boolean outputNullInMain_tDataMasking_15 = false;
						if (row2.CreditCard == null) {
							isInputNull_tDataMasking_15 = true;
						} else if ("".equals(row2.CreditCard.toString().trim())) {
							isInputEmpty_tDataMasking_15 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_15) || (false && isInputEmpty_tDataMasking_15)) {
							tmpValue_tDataMasking_1 = row2.CreditCard;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_15.generateMaskedRow(row2.CreditCard);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_15)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_15) && (false || false))) {
								/*
								 * when doesn't check "send invalid data to invalid flow", then all data in the
								 * main flow, so the invalid values should show null
								 */
								row2.CreditCard = null;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row2.CreditCard = null;
								}

							} else {

								row2.CreditCard = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_16 = false;
						boolean isInputEmpty_tDataMasking_16 = false;
						boolean outputNullInMain_tDataMasking_16 = false;
						if (row2.email == null) {
							isInputNull_tDataMasking_16 = true;
						} else if ("".equals(row2.email.toString().trim())) {
							isInputEmpty_tDataMasking_16 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_16) || (false && isInputEmpty_tDataMasking_16)) {
							tmpValue_tDataMasking_1 = row2.email;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_16.generateMaskedRow(row2.email);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_16)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_16)
											&& (checkByChars(tmpValue_tDataMasking_1, "x") || false))) {
								/*
								 * when doesn't check "send invalid data to invalid flow", then all data in the
								 * main flow, so the invalid values should show null
								 */
								row2.email = null;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row2.email = null;
								}

							} else {

								row2.email = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

					}

					private boolean invalidForMaskByList(Object inputData, String theListStr) {
						if (inputData == null) {
							return true;
						}
						return theListStr.toString().indexOf(inputData.toString()) >= 0;
					}

					// only worked for Email Masking: check if the current column set the special
					// masked char (used for mask)
					private boolean checkByChars(Object inputData, String maskChar) {

						if (inputData == null) {
							return true;
						}
						if (maskChar == null || "".equals(maskChar)) {
							maskChar = "X";
						}
						String maskResult = inputData.toString();
						return maskResult.matches(maskChar + "{" + maskResult.length() + "}");
					}
				};

				/**
				 * [tDataMasking_1 begin ] stop
				 */

				/**
				 * [tFileInputDelimited_1 begin ] start
				 */

				sh("tFileInputDelimited_1");

				s(currentComponent = "tFileInputDelimited_1");

				cLabel = "data_masking";

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
							log4jParamters_tFileInputDelimited_1.append("FILENAME" + " = " + "context.DATA_FILE");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("HEADER" + " = " + "1");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("FOOTER" + " = " + "0");
							log4jParamters_tFileInputDelimited_1.append(" | ");
							log4jParamters_tFileInputDelimited_1.append("LIMIT" + " = " + "");
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
									+ ", SCHEMA_COLUMN=" + ("FirstName") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("LastName") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Address")
									+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("SSN") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("CreditCard") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("email") + "}]");
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
					talendJobLog.addCM("tFileInputDelimited_1", "data_masking", "tFileInputDelimited");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				final routines.system.RowState rowstate_tFileInputDelimited_1 = new routines.system.RowState();

				int nb_line_tFileInputDelimited_1 = 0;
				org.talend.fileprocess.FileInputDelimited fid_tFileInputDelimited_1 = null;
				int limit_tFileInputDelimited_1 = -1;
				try {

					Object filename_tFileInputDelimited_1 = context.DATA_FILE;
					if (filename_tFileInputDelimited_1 instanceof java.io.InputStream) {

						int footer_value_tFileInputDelimited_1 = 0, random_value_tFileInputDelimited_1 = -1;
						if (footer_value_tFileInputDelimited_1 > 0 || random_value_tFileInputDelimited_1 > 0) {
							throw new java.lang.Exception(
									"When the input source is a stream,footer and random shouldn't be bigger than 0.");
						}

					}
					try {
						fid_tFileInputDelimited_1 = new org.talend.fileprocess.FileInputDelimited(context.DATA_FILE,
								"US-ASCII", ";", "\n", false, 1, 0, limit_tFileInputDelimited_1, -1, false);
					} catch (java.lang.Exception e) {
						globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE", e.getMessage());

						log.error("tFileInputDelimited_1 - " + e.getMessage());

						System.err.println(e.getMessage());

					}

					log.info("tFileInputDelimited_1 - Retrieving records from the datasource.");

					while (fid_tFileInputDelimited_1 != null && fid_tFileInputDelimited_1.nextRecord()) {
						rowstate_tFileInputDelimited_1.reset();

						row3 = null;

						boolean whetherReject_tFileInputDelimited_1 = false;
						row3 = new row3Struct();
						try {

							int columnIndexWithD_tFileInputDelimited_1 = 0;

							columnIndexWithD_tFileInputDelimited_1 = 0;

							row3.FirstName = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 1;

							row3.LastName = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 2;

							row3.Address = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 3;

							row3.SSN = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 4;

							row3.CreditCard = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							columnIndexWithD_tFileInputDelimited_1 = 5;

							row3.email = fid_tFileInputDelimited_1.get(columnIndexWithD_tFileInputDelimited_1);

							if (rowstate_tFileInputDelimited_1.getException() != null) {
								throw rowstate_tFileInputDelimited_1.getException();
							}

						} catch (java.lang.Exception e) {
							globalMap.put("tFileInputDelimited_1_ERROR_MESSAGE", e.getMessage());
							whetherReject_tFileInputDelimited_1 = true;

							log.error("tFileInputDelimited_1 - " + e.getMessage());

							System.err.println(e.getMessage());
							row3 = null;

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

						cLabel = "data_masking";

						tos_count_tFileInputDelimited_1++;

						/**
						 * [tFileInputDelimited_1 main ] stop
						 */

						/**
						 * [tFileInputDelimited_1 process_data_begin ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "data_masking";

						/**
						 * [tFileInputDelimited_1 process_data_begin ] stop
						 */

// Start of branch "row3"
						if (row3 != null) {

							/**
							 * [tDataMasking_1 main ] start
							 */

							s(currentComponent = "tDataMasking_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row3", "tFileInputDelimited_1", "data_masking", "tFileInputDelimited",
									"tDataMasking_1", "tDataMasking_1", "tDataMasking"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row3 - " + (row3 == null ? "" : row3.toLogString()));
							}

							errorMessage_tDataMasking_1.ERROR_MESSAGE = null;
							List<row2Struct> row2ReslutList = duplicator_tDataMasking_1.process(row3, false);

							for (row2Struct tmpStructMask_tDataMasking_1 : row2ReslutList) {
								if (errorMessage_tDataMasking_1 == null
										|| errorMessage_tDataMasking_1.ERROR_MESSAGE == null) {
									row2 = tmpStructMask_tDataMasking_1;
								} else {
									row2 = null;
								}

								tos_count_tDataMasking_1++;

								/**
								 * [tDataMasking_1 main ] stop
								 */

								/**
								 * [tDataMasking_1 process_data_begin ] start
								 */

								s(currentComponent = "tDataMasking_1");

								/**
								 * [tDataMasking_1 process_data_begin ] stop
								 */

// Start of branch "row2"
								if (row2 != null) {

									/**
									 * [tLogRow_1 main ] start
									 */

									s(currentComponent = "tLogRow_1");

									if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

											, "row2", "tDataMasking_1", "tDataMasking_1", "tDataMasking", "tLogRow_1",
											"tLogRow_1", "tLogRow"

									)) {
										talendJobLogProcess(globalMap);
									}

									if (log.isTraceEnabled()) {
										log.trace("row2 - " + (row2 == null ? "" : row2.toLogString()));
									}

///////////////////////		

									String[] row_tLogRow_1 = new String[7];

									if (row2.FirstName != null) { //
										row_tLogRow_1[0] = String.valueOf(row2.FirstName);

									} //

									if (row2.LastName != null) { //
										row_tLogRow_1[1] = String.valueOf(row2.LastName);

									} //

									if (row2.Address != null) { //
										row_tLogRow_1[2] = String.valueOf(row2.Address);

									} //

									if (row2.SSN != null) { //
										row_tLogRow_1[3] = String.valueOf(row2.SSN);

									} //

									if (row2.CreditCard != null) { //
										row_tLogRow_1[4] = String.valueOf(row2.CreditCard);

									} //

									if (row2.email != null) { //
										row_tLogRow_1[5] = String.valueOf(row2.email);

									} //

									if (row2.ORIGINAL_MARK != null) { //
										row_tLogRow_1[6] = String.valueOf(row2.ORIGINAL_MARK);

									} //

									util_tLogRow_1.addRow(row_tLogRow_1);
									nb_line_tLogRow_1++;
									log.info("tLogRow_1 - Content of row " + nb_line_tLogRow_1 + ": "
											+ TalendString.unionString("|", row_tLogRow_1));
//////

//////                    

///////////////////////    			

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
									 * [tLogRow_1 process_data_end ] start
									 */

									s(currentComponent = "tLogRow_1");

									/**
									 * [tLogRow_1 process_data_end ] stop
									 */

								} // End of branch "row2"

								// end for
							}

							/**
							 * [tDataMasking_1 process_data_end ] start
							 */

							s(currentComponent = "tDataMasking_1");

							/**
							 * [tDataMasking_1 process_data_end ] stop
							 */

						} // End of branch "row3"

						/**
						 * [tFileInputDelimited_1 process_data_end ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "data_masking";

						/**
						 * [tFileInputDelimited_1 process_data_end ] stop
						 */

						/**
						 * [tFileInputDelimited_1 end ] start
						 */

						s(currentComponent = "tFileInputDelimited_1");

						cLabel = "data_masking";

					}
				} finally {
					if (!((Object) (context.DATA_FILE) instanceof java.io.InputStream)) {
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
				 * [tDataMasking_1 end ] start
				 */

				s(currentComponent = "tDataMasking_1");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row3", 2, 0,
						"tFileInputDelimited_1", "data_masking", "tFileInputDelimited", "tDataMasking_1",
						"tDataMasking_1", "tDataMasking", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tDataMasking_1 - " + ("Done."));

				ok_Hash.put("tDataMasking_1", true);
				end_Hash.put("tDataMasking_1", System.currentTimeMillis());

				/**
				 * [tDataMasking_1 end ] stop
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
						"tDataMasking_1", "tDataMasking_1", "tDataMasking", "tLogRow_1", "tLogRow_1", "tLogRow",
						"output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tLogRow_1 - " + ("Done."));

				ok_Hash.put("tLogRow_1", true);
				end_Hash.put("tLogRow_1", System.currentTimeMillis());

				/**
				 * [tLogRow_1 end ] stop
				 */

			} // end the resume

			if (resumeEntryMethodName == null || globalResumeTicket) {
				resumeUtil.addLog("CHECKPOINT", "CONNECTION:SUBJOB_OK:tFileInputDelimited_1:OnSubjobOk", "",
						Thread.currentThread().getId() + "", "", "", "", "", "");
			}

			if (execStat) {
				runStat.updateStatOnConnection("OnSubjobOk2", 0, "ok");
			}

			tFileInputDelimited_2Process(globalMap);

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

				cLabel = "data_masking";

				/**
				 * [tFileInputDelimited_1 finally ] stop
				 */

				/**
				 * [tDataMasking_1 finally ] start
				 */

				s(currentComponent = "tDataMasking_1");

				/**
				 * [tDataMasking_1 finally ] stop
				 */

				/**
				 * [tLogRow_1 finally ] start
				 */

				s(currentComponent = "tLogRow_1");

				/**
				 * [tLogRow_1 finally ] stop
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

	public static class row5Struct implements routines.system.IPersistableRow<row5Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];

		public String FirstName;

		public String getFirstName() {
			return this.FirstName;
		}

		public Boolean FirstNameIsNullable() {
			return true;
		}

		public Boolean FirstNameIsKey() {
			return false;
		}

		public Integer FirstNameLength() {
			return 7;
		}

		public Integer FirstNamePrecision() {
			return 0;
		}

		public String FirstNameDefault() {

			return null;

		}

		public String FirstNameComment() {

			return "";

		}

		public String FirstNamePattern() {

			return "dd-MM-yyyy";

		}

		public String FirstNameOriginalDbColumnName() {

			return "FirstName";

		}

		public String LastName;

		public String getLastName() {
			return this.LastName;
		}

		public Boolean LastNameIsNullable() {
			return true;
		}

		public Boolean LastNameIsKey() {
			return false;
		}

		public Integer LastNameLength() {
			return 9;
		}

		public Integer LastNamePrecision() {
			return 0;
		}

		public String LastNameDefault() {

			return null;

		}

		public String LastNameComment() {

			return "";

		}

		public String LastNamePattern() {

			return "dd-MM-yyyy";

		}

		public String LastNameOriginalDbColumnName() {

			return "LastName";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 21;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

		}

		public String AddressPattern() {

			return "dd-MM-yyyy";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

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
			return null;
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
			return null;
		}

		public Integer CreditCardPrecision() {
			return 0;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "dd-MM-yyyy";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		public Boolean emailIsNullable() {
			return true;
		}

		public Boolean emailIsKey() {
			return false;
		}

		public Integer emailLength() {
			return 16;
		}

		public Integer emailPrecision() {
			return 0;
		}

		public String emailDefault() {

			return null;

		}

		public String emailComment() {

			return "";

		}

		public String emailPattern() {

			return "dd-MM-yyyy";

		}

		public String emailOriginalDbColumnName() {

			return "email";

		}

		public String T_GEN_KEY;

		public String getT_GEN_KEY() {
			return this.T_GEN_KEY;
		}

		public Boolean T_GEN_KEYIsNullable() {
			return true;
		}

		public Boolean T_GEN_KEYIsKey() {
			return false;
		}

		public Integer T_GEN_KEYLength() {
			return 255;
		}

		public Integer T_GEN_KEYPrecision() {
			return 0;
		}

		public String T_GEN_KEYDefault() {

			return "";

		}

		public String T_GEN_KEYComment() {

			return null;

		}

		public String T_GEN_KEYPattern() {

			return null;

		}

		public String T_GEN_KEYOriginalDbColumnName() {

			return "T_GEN_KEY";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

					this.T_GEN_KEY = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

					this.T_GEN_KEY = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

				// String

				writeString(this.T_GEN_KEY, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

				// String

				writeString(this.T_GEN_KEY, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",Address=" + Address);
			sb.append(",SSN=" + SSN);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",email=" + email);
			sb.append(",T_GEN_KEY=" + T_GEN_KEY);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (FirstName == null) {
				sb.append("<null>");
			} else {
				sb.append(FirstName);
			}

			sb.append("|");

			if (LastName == null) {
				sb.append("<null>");
			} else {
				sb.append(LastName);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
			}

			sb.append("|");

			if (T_GEN_KEY == null) {
				sb.append("<null>");
			} else {
				sb.append(T_GEN_KEY);
			}

			sb.append("|");

			return sb.toString();
		}

		/**
		 * Compare keys
		 */
		public int compareTo(row5Struct other) {

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

	public static class row4Struct implements routines.system.IPersistableRow<row4Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[0];

		public String FirstName;

		public String getFirstName() {
			return this.FirstName;
		}

		public Boolean FirstNameIsNullable() {
			return true;
		}

		public Boolean FirstNameIsKey() {
			return false;
		}

		public Integer FirstNameLength() {
			return 7;
		}

		public Integer FirstNamePrecision() {
			return 0;
		}

		public String FirstNameDefault() {

			return null;

		}

		public String FirstNameComment() {

			return "";

		}

		public String FirstNamePattern() {

			return "dd-MM-yyyy";

		}

		public String FirstNameOriginalDbColumnName() {

			return "FirstName";

		}

		public String LastName;

		public String getLastName() {
			return this.LastName;
		}

		public Boolean LastNameIsNullable() {
			return true;
		}

		public Boolean LastNameIsKey() {
			return false;
		}

		public Integer LastNameLength() {
			return 9;
		}

		public Integer LastNamePrecision() {
			return 0;
		}

		public String LastNameDefault() {

			return null;

		}

		public String LastNameComment() {

			return "";

		}

		public String LastNamePattern() {

			return "dd-MM-yyyy";

		}

		public String LastNameOriginalDbColumnName() {

			return "LastName";

		}

		public String Address;

		public String getAddress() {
			return this.Address;
		}

		public Boolean AddressIsNullable() {
			return true;
		}

		public Boolean AddressIsKey() {
			return false;
		}

		public Integer AddressLength() {
			return 21;
		}

		public Integer AddressPrecision() {
			return 0;
		}

		public String AddressDefault() {

			return null;

		}

		public String AddressComment() {

			return "";

		}

		public String AddressPattern() {

			return "dd-MM-yyyy";

		}

		public String AddressOriginalDbColumnName() {

			return "Address";

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
			return null;
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
			return null;
		}

		public Integer CreditCardPrecision() {
			return 0;
		}

		public String CreditCardDefault() {

			return null;

		}

		public String CreditCardComment() {

			return "";

		}

		public String CreditCardPattern() {

			return "dd-MM-yyyy";

		}

		public String CreditCardOriginalDbColumnName() {

			return "CreditCard";

		}

		public String email;

		public String getEmail() {
			return this.email;
		}

		public Boolean emailIsNullable() {
			return true;
		}

		public Boolean emailIsKey() {
			return false;
		}

		public Integer emailLength() {
			return 16;
		}

		public Integer emailPrecision() {
			return 0;
		}

		public String emailDefault() {

			return null;

		}

		public String emailComment() {

			return "";

		}

		public String emailPattern() {

			return "dd-MM-yyyy";

		}

		public String emailOriginalDbColumnName() {

			return "email";

		}

		private String readString(ObjectInputStream dis) throws IOException {
			String strReturn = null;
			int length = 0;
			length = dis.readInt();
			if (length == -1) {
				strReturn = null;
			} else {
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_create_data_and_mask_with_key.length == 0) {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_create_data_and_mask_with_key = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_create_data_and_mask_with_key, 0, length,
						utf8Charset);
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

		public void readData(ObjectInputStream dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_create_data_and_mask_with_key) {

				try {

					int length = 0;

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					this.Address = readString(dis);

					this.SSN = readString(dis);

					this.CreditCard = readString(dis);

					this.email = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void writeData(ObjectOutputStream dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// String

				writeString(this.Address, dos);

				// String

				writeString(this.SSN, dos);

				// String

				writeString(this.CreditCard, dos);

				// String

				writeString(this.email, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",Address=" + Address);
			sb.append(",SSN=" + SSN);
			sb.append(",CreditCard=" + CreditCard);
			sb.append(",email=" + email);
			sb.append("]");

			return sb.toString();
		}

		public String toLogString() {
			StringBuilder sb = new StringBuilder();

			if (FirstName == null) {
				sb.append("<null>");
			} else {
				sb.append(FirstName);
			}

			sb.append("|");

			if (LastName == null) {
				sb.append("<null>");
			} else {
				sb.append(LastName);
			}

			sb.append("|");

			if (Address == null) {
				sb.append("<null>");
			} else {
				sb.append(Address);
			}

			sb.append("|");

			if (SSN == null) {
				sb.append("<null>");
			} else {
				sb.append(SSN);
			}

			sb.append("|");

			if (CreditCard == null) {
				sb.append("<null>");
			} else {
				sb.append(CreditCard);
			}

			sb.append("|");

			if (email == null) {
				sb.append("<null>");
			} else {
				sb.append(email);
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

	public void tFileInputDelimited_2Process(final java.util.Map<String, Object> globalMap) throws TalendException {
		globalMap.put("tFileInputDelimited_2_SUBPROCESS_STATE", 0);

		final boolean execStat = this.execStat;

		mdc("tFileInputDelimited_2", "UGsIA9_");

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

				row4Struct row4 = new row4Struct();
				row5Struct row5 = new row5Struct();

				/**
				 * [tLogRow_2 begin ] start
				 */

				sh("tLogRow_2");

				s(currentComponent = "tLogRow_2");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row5");

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

					int[] colLengths = new int[7];

					public void addRow(String[] row) {

						for (int i = 0; i < 7; i++) {
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
						for (k = 0; k < (totals + 6 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 6 - name.length() - k; i++) {
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

						// last column
						for (int i = 0; i < colLengths[6] - fillChars[1].length() + 1; i++) {
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
				util_tLogRow_2.addRow(new String[] { "FirstName", "LastName", "Address", "SSN", "CreditCard", "email",
						"T_GEN_KEY", });
				StringBuilder strBuffer_tLogRow_2 = null;
				int nb_line_tLogRow_2 = 0;
///////////////////////    			

				/**
				 * [tLogRow_2 begin ] stop
				 */

				/**
				 * [tGenKey_1 begin ] start
				 */

				sh("tGenKey_1");

				s(currentComponent = "tGenKey_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row4");

				int tos_count_tGenKey_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tGenKey_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tGenKey_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tGenKey_1 = new StringBuilder();
							log4jParamters_tGenKey_1.append("Parameters:");
							log4jParamters_tGenKey_1.append("ALGO" + " = " + "[{POST_ALGO=" + ("NON_ALGO")
									+ ", POST_VALUE=" + ("") + ", PRECOLUMN=" + ("SSN") + ", PRE_ALGO=" + ("NON_ALGO")
									+ ", PRE_VALUE=" + ("") + ", KEY_ALGO=" + ("exact") + ", KEY_VALUE=" + ("")
									+ "}, {POST_ALGO=" + ("NON_ALGO") + ", POST_VALUE=" + ("") + ", PRECOLUMN="
									+ ("FirstName") + ", PRE_ALGO=" + ("NON_ALGO") + ", PRE_VALUE=" + ("")
									+ ", KEY_ALGO=" + ("first_Char_EW") + ", KEY_VALUE=" + ("") + "}]");
							log4jParamters_tGenKey_1.append(" | ");
							log4jParamters_tGenKey_1.append("SHOW_HELP_KEY" + " = " + "false");
							log4jParamters_tGenKey_1.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tGenKey_1 - " + (log4jParamters_tGenKey_1));
						}
					}
					new BytesLimit65535_tGenKey_1().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tGenKey_1", "tGenKey_1", "tGenKey");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				/**
				 * [tGenKey_1 begin ] stop
				 */

				/**
				 * [tFileInputDelimited_2 begin ] start
				 */

				sh("tFileInputDelimited_2");

				s(currentComponent = "tFileInputDelimited_2");

				cLabel = "data_masking";

				int tos_count_tFileInputDelimited_2 = 0;

				if (log.isDebugEnabled())
					log.debug("tFileInputDelimited_2 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tFileInputDelimited_2 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tFileInputDelimited_2 = new StringBuilder();
							log4jParamters_tFileInputDelimited_2.append("Parameters:");
							log4jParamters_tFileInputDelimited_2.append("USE_EXISTING_DYNAMIC" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("FILENAME" + " = " + "context.DATA_FILE");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("CSV_OPTION" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("ROWSEPARATOR" + " = " + "\"\\n\"");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("FIELDSEPARATOR" + " = " + "\";\"");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("HEADER" + " = " + "1");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("FOOTER" + " = " + "0");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("LIMIT" + " = " + "");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("REMOVE_EMPTY_ROW" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("UNCOMPRESS" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("DIE_ON_ERROR" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("ADVANCED_SEPARATOR" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("RANDOM" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("TRIMALL" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("TRIMSELECT" + " = " + "[{TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("FirstName") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("LastName") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("Address")
									+ "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN=" + ("SSN") + "}, {TRIM=" + ("false")
									+ ", SCHEMA_COLUMN=" + ("CreditCard") + "}, {TRIM=" + ("false") + ", SCHEMA_COLUMN="
									+ ("email") + "}]");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("CHECK_FIELDS_NUM" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("CHECK_DATE" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("ENCODING" + " = " + "\"US-ASCII\"");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("SPLITRECORD" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("ENABLE_DECODE" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							log4jParamters_tFileInputDelimited_2.append("USE_HEADER_AS_IS" + " = " + "false");
							log4jParamters_tFileInputDelimited_2.append(" | ");
							if (log.isDebugEnabled())
								log.debug("tFileInputDelimited_2 - " + (log4jParamters_tFileInputDelimited_2));
						}
					}
					new BytesLimit65535_tFileInputDelimited_2().limitLog4jByte();
				}
				if (enableLogStash) {
					talendJobLog.addCM("tFileInputDelimited_2", "data_masking", "tFileInputDelimited");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				final routines.system.RowState rowstate_tFileInputDelimited_2 = new routines.system.RowState();

				int nb_line_tFileInputDelimited_2 = 0;
				org.talend.fileprocess.FileInputDelimited fid_tFileInputDelimited_2 = null;
				int limit_tFileInputDelimited_2 = -1;
				try {

					Object filename_tFileInputDelimited_2 = context.DATA_FILE;
					if (filename_tFileInputDelimited_2 instanceof java.io.InputStream) {

						int footer_value_tFileInputDelimited_2 = 0, random_value_tFileInputDelimited_2 = -1;
						if (footer_value_tFileInputDelimited_2 > 0 || random_value_tFileInputDelimited_2 > 0) {
							throw new java.lang.Exception(
									"When the input source is a stream,footer and random shouldn't be bigger than 0.");
						}

					}
					try {
						fid_tFileInputDelimited_2 = new org.talend.fileprocess.FileInputDelimited(context.DATA_FILE,
								"US-ASCII", ";", "\n", false, 1, 0, limit_tFileInputDelimited_2, -1, false);
					} catch (java.lang.Exception e) {
						globalMap.put("tFileInputDelimited_2_ERROR_MESSAGE", e.getMessage());

						log.error("tFileInputDelimited_2 - " + e.getMessage());

						System.err.println(e.getMessage());

					}

					log.info("tFileInputDelimited_2 - Retrieving records from the datasource.");

					while (fid_tFileInputDelimited_2 != null && fid_tFileInputDelimited_2.nextRecord()) {
						rowstate_tFileInputDelimited_2.reset();

						row4 = null;

						boolean whetherReject_tFileInputDelimited_2 = false;
						row4 = new row4Struct();
						try {

							int columnIndexWithD_tFileInputDelimited_2 = 0;

							columnIndexWithD_tFileInputDelimited_2 = 0;

							row4.FirstName = fid_tFileInputDelimited_2.get(columnIndexWithD_tFileInputDelimited_2);

							columnIndexWithD_tFileInputDelimited_2 = 1;

							row4.LastName = fid_tFileInputDelimited_2.get(columnIndexWithD_tFileInputDelimited_2);

							columnIndexWithD_tFileInputDelimited_2 = 2;

							row4.Address = fid_tFileInputDelimited_2.get(columnIndexWithD_tFileInputDelimited_2);

							columnIndexWithD_tFileInputDelimited_2 = 3;

							row4.SSN = fid_tFileInputDelimited_2.get(columnIndexWithD_tFileInputDelimited_2);

							columnIndexWithD_tFileInputDelimited_2 = 4;

							row4.CreditCard = fid_tFileInputDelimited_2.get(columnIndexWithD_tFileInputDelimited_2);

							columnIndexWithD_tFileInputDelimited_2 = 5;

							row4.email = fid_tFileInputDelimited_2.get(columnIndexWithD_tFileInputDelimited_2);

							if (rowstate_tFileInputDelimited_2.getException() != null) {
								throw rowstate_tFileInputDelimited_2.getException();
							}

						} catch (java.lang.Exception e) {
							globalMap.put("tFileInputDelimited_2_ERROR_MESSAGE", e.getMessage());
							whetherReject_tFileInputDelimited_2 = true;

							log.error("tFileInputDelimited_2 - " + e.getMessage());

							System.err.println(e.getMessage());
							row4 = null;

						}

						log.debug("tFileInputDelimited_2 - Retrieving the record "
								+ fid_tFileInputDelimited_2.getRowNumber() + ".");

						/**
						 * [tFileInputDelimited_2 begin ] stop
						 */

						/**
						 * [tFileInputDelimited_2 main ] start
						 */

						s(currentComponent = "tFileInputDelimited_2");

						cLabel = "data_masking";

						tos_count_tFileInputDelimited_2++;

						/**
						 * [tFileInputDelimited_2 main ] stop
						 */

						/**
						 * [tFileInputDelimited_2 process_data_begin ] start
						 */

						s(currentComponent = "tFileInputDelimited_2");

						cLabel = "data_masking";

						/**
						 * [tFileInputDelimited_2 process_data_begin ] stop
						 */

// Start of branch "row4"
						if (row4 != null) {

							/**
							 * [tGenKey_1 main ] start
							 */

							s(currentComponent = "tGenKey_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row4", "tFileInputDelimited_2", "data_masking", "tFileInputDelimited",
									"tGenKey_1", "tGenKey_1", "tGenKey"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row4 - " + (row4 == null ? "" : row4.toLogString()));
							}

							String winKey_tGenKey_1 = "";
							String strInput_tGenKey_1 = null;
							strInput_tGenKey_1 = "";
							strInput_tGenKey_1 = TypeConvert.String2String(row4.SSN);
							strInput_tGenKey_1 = org.talend.windowkey.AlgoBox.exact(strInput_tGenKey_1);
							winKey_tGenKey_1 += (strInput_tGenKey_1 == null) ? "" : strInput_tGenKey_1;
							strInput_tGenKey_1 = "";
							strInput_tGenKey_1 = TypeConvert.String2String(row4.FirstName);
							strInput_tGenKey_1 = org.talend.windowkey.AlgoBox.first_Char_EW(strInput_tGenKey_1);
							winKey_tGenKey_1 += (strInput_tGenKey_1 == null) ? "" : strInput_tGenKey_1;
							row5.FirstName = row4.FirstName;
							row5.LastName = row4.LastName;
							row5.Address = row4.Address;
							row5.SSN = row4.SSN;
							row5.CreditCard = row4.CreditCard;
							row5.email = row4.email;
							row5.T_GEN_KEY = winKey_tGenKey_1;

							tos_count_tGenKey_1++;

							/**
							 * [tGenKey_1 main ] stop
							 */

							/**
							 * [tGenKey_1 process_data_begin ] start
							 */

							s(currentComponent = "tGenKey_1");

							/**
							 * [tGenKey_1 process_data_begin ] stop
							 */

							/**
							 * [tLogRow_2 main ] start
							 */

							s(currentComponent = "tLogRow_2");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row5", "tGenKey_1", "tGenKey_1", "tGenKey", "tLogRow_2", "tLogRow_2", "tLogRow"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row5 - " + (row5 == null ? "" : row5.toLogString()));
							}

///////////////////////		

							String[] row_tLogRow_2 = new String[7];

							if (row5.FirstName != null) { //
								row_tLogRow_2[0] = String.valueOf(row5.FirstName);

							} //

							if (row5.LastName != null) { //
								row_tLogRow_2[1] = String.valueOf(row5.LastName);

							} //

							if (row5.Address != null) { //
								row_tLogRow_2[2] = String.valueOf(row5.Address);

							} //

							if (row5.SSN != null) { //
								row_tLogRow_2[3] = String.valueOf(row5.SSN);

							} //

							if (row5.CreditCard != null) { //
								row_tLogRow_2[4] = String.valueOf(row5.CreditCard);

							} //

							if (row5.email != null) { //
								row_tLogRow_2[5] = String.valueOf(row5.email);

							} //

							if (row5.T_GEN_KEY != null) { //
								row_tLogRow_2[6] = String.valueOf(row5.T_GEN_KEY);

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
							 * [tGenKey_1 process_data_end ] start
							 */

							s(currentComponent = "tGenKey_1");

							/**
							 * [tGenKey_1 process_data_end ] stop
							 */

						} // End of branch "row4"

						/**
						 * [tFileInputDelimited_2 process_data_end ] start
						 */

						s(currentComponent = "tFileInputDelimited_2");

						cLabel = "data_masking";

						/**
						 * [tFileInputDelimited_2 process_data_end ] stop
						 */

						/**
						 * [tFileInputDelimited_2 end ] start
						 */

						s(currentComponent = "tFileInputDelimited_2");

						cLabel = "data_masking";

					}
				} finally {
					if (!((Object) (context.DATA_FILE) instanceof java.io.InputStream)) {
						if (fid_tFileInputDelimited_2 != null) {
							fid_tFileInputDelimited_2.close();
						}
					}
					if (fid_tFileInputDelimited_2 != null) {
						globalMap.put("tFileInputDelimited_2_NB_LINE", fid_tFileInputDelimited_2.getRowNumber());

						log.info("tFileInputDelimited_2 - Retrieved records count: "
								+ fid_tFileInputDelimited_2.getRowNumber() + ".");

					}
				}

				if (log.isDebugEnabled())
					log.debug("tFileInputDelimited_2 - " + ("Done."));

				ok_Hash.put("tFileInputDelimited_2", true);
				end_Hash.put("tFileInputDelimited_2", System.currentTimeMillis());

				/**
				 * [tFileInputDelimited_2 end ] stop
				 */

				/**
				 * [tGenKey_1 end ] start
				 */

				s(currentComponent = "tGenKey_1");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row4", 2, 0,
						"tFileInputDelimited_2", "data_masking", "tFileInputDelimited", "tGenKey_1", "tGenKey_1",
						"tGenKey", "output")) {
					talendJobLogProcess(globalMap);
				}

				if (log.isDebugEnabled())
					log.debug("tGenKey_1 - " + ("Done."));

				ok_Hash.put("tGenKey_1", true);
				end_Hash.put("tGenKey_1", System.currentTimeMillis());

				/**
				 * [tGenKey_1 end ] stop
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

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row5", 2, 0,
						"tGenKey_1", "tGenKey_1", "tGenKey", "tLogRow_2", "tLogRow_2", "tLogRow", "output")) {
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
				 * [tFileInputDelimited_2 finally ] start
				 */

				s(currentComponent = "tFileInputDelimited_2");

				cLabel = "data_masking";

				/**
				 * [tFileInputDelimited_2 finally ] stop
				 */

				/**
				 * [tGenKey_1 finally ] start
				 */

				s(currentComponent = "tGenKey_1");

				/**
				 * [tGenKey_1 finally ] stop
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

		globalMap.put("tFileInputDelimited_2_SUBPROCESS_STATE", 1);
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
		final create_data_and_mask_with_key create_data_and_mask_with_keyClass = new create_data_and_mask_with_key();

		int exitCode = create_data_and_mask_with_keyClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'create_data_and_mask_with_key' - Done.");
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
		log.info("TalendJob: 'create_data_and_mask_with_key' - Start.");

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
		org.slf4j.MDC.put("_jobRepositoryId", "_py4uwLJ8EeekK-npoh341w");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2025-06-30T21:34:57.857032Z");

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
			java.io.InputStream inContext = create_data_and_mask_with_key.class.getClassLoader().getResourceAsStream(
					"dataprivacy/create_data_and_mask_with_key_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = create_data_and_mask_with_key.class.getClassLoader()
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
					context.setContextType("DATA_FILE", "id_String");
					if (context.getStringValue("DATA_FILE") == null) {
						context.DATA_FILE = null;
					} else {
						context.DATA_FILE = (String) context.getProperty("DATA_FILE");
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
			if (parentContextMap.containsKey("DATA_FILE")) {
				context.DATA_FILE = (String) parentContextMap.get("DATA_FILE");
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
		log.info("TalendJob: 'create_data_and_mask_with_key' - Started.");
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
			tRowGenerator_1Process(globalMap);
			if (!"failure".equals(status)) {
				status = "end";
			}
		} catch (TalendException e_tRowGenerator_1) {
			globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", -1);

			e_tRowGenerator_1.printStackTrace();

		}

		this.globalResumeTicket = true;// to run tPostJob

		end = System.currentTimeMillis();

		if (watch) {
			System.out.println((end - startTime) + " milliseconds");
		}

		endUsedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		if (false) {
			System.out.println((endUsedMemory - startUsedMemory)
					+ " bytes memory increase when running : create_data_and_mask_with_key");
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
		log.info("TalendJob: 'create_data_and_mask_with_key' - Finished - status: " + status + " returnCode: "
				+ returnCode);

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
 * 229740 characters generated by Talend Cloud Data Management Platform on the
 * June 30, 2025 at 10:34:57 PM BST
 ************************************************************************************************/