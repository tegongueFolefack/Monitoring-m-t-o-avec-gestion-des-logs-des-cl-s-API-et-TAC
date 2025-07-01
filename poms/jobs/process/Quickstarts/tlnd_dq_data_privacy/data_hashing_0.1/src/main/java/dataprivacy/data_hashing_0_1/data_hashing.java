
package dataprivacy.data_hashing_0_1;

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
 * Job: data_hashing Purpose: <br>
 * Description: <br>
 * 
 * @author Habushi, Ofer
 * @version 8.0.1.20250625_0954-patch
 * @status
 */
public class data_hashing implements TalendJob {
	static {
		System.setProperty("TalendJob.log", "data_hashing.log");
	}

	private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager
			.getLogger(data_hashing.class);

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

		}

		// if the stored or passed value is "<TALEND_NULL>" string, it mean null
		public String getStringValue(String key) {
			String origin_value = this.getProperty(key);
			if (NULL_VALUE_EXPRESSION_IN_COMMAND_STRING_FOR_CHILD_JOB_ONLY.equals(origin_value)) {
				return null;
			}
			return origin_value;
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
	private final String jobName = "data_hashing";
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
			"_tu7SMKXAEeqL4ZI8TPRa0w", "0.1");
	private org.talend.job.audit.JobAuditLogger auditLogger_talendJobLog = null;

	private RunStat runStat = new RunStat(talendJobLog, System.getProperty("audit.interval"));
	private RunTrace runTrace = new RunTrace();

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
				data_hashing.this.exception = e;
			}
			if (!(e instanceof TalendException)) {
				try {
					for (java.lang.reflect.Method m : this.getClass().getEnclosingClass().getMethods()) {
						if (m.getName().compareTo(currentComponent + "_error") == 0) {
							m.invoke(data_hashing.this, new Object[] { e, currentComponent, globalMap });
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

	public void tAddCRCRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tDataMasking_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
	}

	public void tLogRow_1_error(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		end_Hash.put(errorComponent, System.currentTimeMillis());

		status = "failure";

		tRowGenerator_1_onSubJobError(exception, errorComponent, globalMap);
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

	public void talendJobLog_onSubJobError(Exception exception, String errorComponent,
			final java.util.Map<String, Object> globalMap) throws TalendException {

		resumeUtil.addLog("SYSTEM_LOG", "NODE:" + errorComponent, "", Thread.currentThread().getId() + "", "FATAL", "",
				exception.getMessage(), ResumeUtil.getExceptionStackTrace(exception), "");

	}

	public static class row3Struct implements routines.system.IPersistableRow<row3Struct> {
		final static byte[] commonByteArrayLock_DATAPRIVACY_data_hashing = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_data_hashing = new byte[0];

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
			return null;
		}

		public Integer IDPrecision() {
			return null;
		}

		public String IDDefault() {

			return "";

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

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

		public Long CRC;

		public Long getCRC() {
			return this.CRC;
		}

		public Boolean CRCIsNullable() {
			return true;
		}

		public Boolean CRCIsKey() {
			return false;
		}

		public Integer CRCLength() {
			return 255;
		}

		public Integer CRCPrecision() {
			return 0;
		}

		public String CRCDefault() {

			return "";

		}

		public String CRCComment() {

			return null;

		}

		public String CRCPattern() {

			return null;

		}

		public String CRCOriginalDbColumnName() {

			return "CRC";

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
				if (length > commonByteArray_DATAPRIVACY_data_hashing.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_data_hashing.length == 0) {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_data_hashing, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_data_hashing, 0, length, utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_data_hashing.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_data_hashing.length == 0) {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_data_hashing, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_data_hashing, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DATAPRIVACY_data_hashing) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.CRC = null;
					} else {
						this.CRC = dis.readLong();
					}

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

			synchronized (commonByteArrayLock_DATAPRIVACY_data_hashing) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.CRC = null;
					} else {
						this.CRC = dis.readLong();
					}

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

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// Long

				if (this.CRC == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.CRC);
				}

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

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// Long

				if (this.CRC == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.CRC);
				}

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
			sb.append(",FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",CRC=" + String.valueOf(CRC));
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

			if (CRC == null) {
				sb.append("<null>");
			} else {
				sb.append(CRC);
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
		final static byte[] commonByteArrayLock_DATAPRIVACY_data_hashing = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_data_hashing = new byte[0];

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
			return null;
		}

		public Integer IDPrecision() {
			return null;
		}

		public String IDDefault() {

			return "";

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

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

		public Long CRC;

		public Long getCRC() {
			return this.CRC;
		}

		public Boolean CRCIsNullable() {
			return true;
		}

		public Boolean CRCIsKey() {
			return false;
		}

		public Integer CRCLength() {
			return 255;
		}

		public Integer CRCPrecision() {
			return 0;
		}

		public String CRCDefault() {

			return "";

		}

		public String CRCComment() {

			return null;

		}

		public String CRCPattern() {

			return null;

		}

		public String CRCOriginalDbColumnName() {

			return "CRC";

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
				if (length > commonByteArray_DATAPRIVACY_data_hashing.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_data_hashing.length == 0) {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_data_hashing, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_data_hashing, 0, length, utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_data_hashing.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_data_hashing.length == 0) {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_data_hashing, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_data_hashing, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DATAPRIVACY_data_hashing) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.CRC = null;
					} else {
						this.CRC = dis.readLong();
					}

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_data_hashing) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

					length = dis.readByte();
					if (length == -1) {
						this.CRC = null;
					} else {
						this.CRC = dis.readLong();
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

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// Long

				if (this.CRC == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.CRC);
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

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

				// Long

				if (this.CRC == null) {
					dos.writeByte(-1);
				} else {
					dos.writeByte(0);
					dos.writeLong(this.CRC);
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
			sb.append(",FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
			sb.append(",CRC=" + String.valueOf(CRC));
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

			if (CRC == null) {
				sb.append("<null>");
			} else {
				sb.append(CRC);
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
		final static byte[] commonByteArrayLock_DATAPRIVACY_data_hashing = new byte[0];
		static byte[] commonByteArray_DATAPRIVACY_data_hashing = new byte[0];

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
			return null;
		}

		public Integer IDPrecision() {
			return null;
		}

		public String IDDefault() {

			return "";

		}

		public String IDComment() {

			return "";

		}

		public String IDPattern() {

			return "";

		}

		public String IDOriginalDbColumnName() {

			return "ID";

		}

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
				if (length > commonByteArray_DATAPRIVACY_data_hashing.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_data_hashing.length == 0) {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[2 * length];
					}
				}
				dis.readFully(commonByteArray_DATAPRIVACY_data_hashing, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_data_hashing, 0, length, utf8Charset);
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
				if (length > commonByteArray_DATAPRIVACY_data_hashing.length) {
					if (length < 1024 && commonByteArray_DATAPRIVACY_data_hashing.length == 0) {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[1024];
					} else {
						commonByteArray_DATAPRIVACY_data_hashing = new byte[2 * length];
					}
				}
				unmarshaller.readFully(commonByteArray_DATAPRIVACY_data_hashing, 0, length);
				strReturn = new String(commonByteArray_DATAPRIVACY_data_hashing, 0, length, utf8Charset);
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

			synchronized (commonByteArrayLock_DATAPRIVACY_data_hashing) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			}

		}

		public void readData(org.jboss.marshalling.Unmarshaller dis) {

			synchronized (commonByteArrayLock_DATAPRIVACY_data_hashing) {

				try {

					int length = 0;

					this.ID = readInteger(dis);

					this.FirstName = readString(dis);

					this.LastName = readString(dis);

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

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public void writeData(org.jboss.marshalling.Marshaller dos) {
			try {

				// Integer

				writeInteger(this.ID, dos);

				// String

				writeString(this.FirstName, dos);

				// String

				writeString(this.LastName, dos);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		public String toString() {

			StringBuilder sb = new StringBuilder();
			sb.append(super.toString());
			sb.append("[");
			sb.append("ID=" + String.valueOf(ID));
			sb.append(",FirstName=" + FirstName);
			sb.append(",LastName=" + LastName);
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

		mdc("tRowGenerator_1", "7xma5H_");

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
				row3Struct row3 = new row3Struct();

				/**
				 * [tLogRow_1 begin ] start
				 */

				globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.FALSE);

				sh("tLogRow_1");

				s(currentComponent = "tLogRow_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row3");

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

					int[] colLengths = new int[5];

					public void addRow(String[] row) {

						for (int i = 0; i < 5; i++) {
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
						for (k = 0; k < (totals + 4 - name.length()) / 2; k++) {
							sb.append(' ');
						}
						sb.append(name);
						for (int i = 0; i < totals + 4 - name.length() - k; i++) {
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

						// last column
						for (int i = 0; i < colLengths[4] - fillChars[1].length() + 1; i++) {
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
				util_tLogRow_1.addRow(new String[] { "ID", "FirstName", "LastName", "CRC", "ORIGINAL_MARK", });
				StringBuilder strBuffer_tLogRow_1 = null;
				int nb_line_tLogRow_1 = 0;
///////////////////////    			

				/**
				 * [tLogRow_1 begin ] stop
				 */

				/**
				 * [tDataMasking_1 begin ] start
				 */

				globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.FALSE);

				sh("tDataMasking_1");

				s(currentComponent = "tDataMasking_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row2");

				int tos_count_tDataMasking_1 = 0;

				if (log.isDebugEnabled())
					log.debug("tDataMasking_1 - " + ("Start to work."));
				if (log.isDebugEnabled()) {
					class BytesLimit65535_tDataMasking_1 {
						public void limitLog4jByte() throws Exception {
							StringBuilder log4jParamters_tDataMasking_1 = new StringBuilder();
							log4jParamters_tDataMasking_1.append("Parameters:");
							log4jParamters_tDataMasking_1.append("MODIF_TABLE" + " = " + "[{EXTRA_PARAMETER=" + ("")
									+ ", CATEGORY=" + ("CHARACTER_HANDLING") + ", METHOD=" + ("SHA2_HMAC_PRF")
									+ ", INPUT_COLUMN=" + ("LastName") + ", FUNCTION=" + ("REPLACE_ALL") + ", ALPHABET="
									+ ("BEST_GUESS") + ", KEEP_FORMAT=" + ("false") + "}]");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("PASSWORD" + " = "
									+ String.valueOf(
											"enc:routine.encryption.key.v1:fIusiEObbofWpyXqEWOAskuIKTA5QA2QniYQCg==")
											.substring(0, 4)
									+ "...");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("USE_TWEAK" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("ALGO_VERSION" + " = " + "1");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("RANDOM_SEED" + " = " + "");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("ENCODING" + " = " + "\"UTF-8\"");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("OUTPUT_ORIGINAL" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("KEEP_NULL" + " = " + "true");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("KEEP_EMPTY" + " = " + "false");
							log4jParamters_tDataMasking_1.append(" | ");
							log4jParamters_tDataMasking_1.append("SEPERATE_OUTPUT" + " = " + "true");
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
				final java.util.Random rnd_tDataMasking_1 = new java.util.Random();

				String value_tDataMasking_11 = null;
				int type_tDataMasking_11 = typeTester_tDataMasking_1.getType(value_tDataMasking_11);

				@SuppressWarnings("unchecked")
				final org.talend.dataquality.datamasking.functions.Function<String> fun_tDataMasking_11 = (org.talend.dataquality.datamasking.functions.Function<String>) fun_fact_tDataMasking_1
						.getFunction("REPLACE_ALL", type_tDataMasking_11, "SHA2_HMAC_PRF");
				fun_tDataMasking_11
						.setMaskingMode(org.talend.dataquality.datamasking.FunctionMode.valueOf("BIJECTIVE"));

				fun_tDataMasking_11.parse(null, true, rnd_tDataMasking_1);

				if (org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
						.isInstance(fun_tDataMasking_11)) {
					fun_tDataMasking_11.setAlphabet(
							org.talend.dataquality.datamasking.functions.text.Alphabet.valueOf("BEST_GUESS"));
				}
				fun_tDataMasking_11.setAlgoVersion(1);

				if (org.apache.commons.lang3.EnumUtils
						.isValidEnum(org.talend.dataquality.datamasking.FormatPreservingMethod.class, "SHA2_HMAC_PRF")
						&& (org.talend.dataquality.datamasking.functions.AbstractGenerateWithSecret.class
								.isInstance(fun_tDataMasking_11)
								|| org.talend.dataquality.datamasking.functions.text.CharactersOperation.class
										.isInstance(fun_tDataMasking_11)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepFirstDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_11)
								|| org.talend.dataquality.datamasking.functions.text.keep.KeepLastDigitsAndReplaceOtherDigits.class
										.isInstance(fun_tDataMasking_11))) {
					fun_tDataMasking_11.setFpeProperties(
							org.talend.dataquality.datamasking.FormatPreservingMethod.valueOf("SHA2_HMAC_PRF"),
							routines.system.PasswordEncryptUtil.decryptPassword(
									"enc:routine.encryption.key.v1:l/Qs6z1KWdg5ccfTzfXh9Shb/Mxp7Pl34GWymA=="),
							org.talend.dataquality.encryption.FF1Cipher.Mode.ENCRYPT);
				}
				// fun_tDataMasking_11.setRandom(rnd_tDataMasking_1);
				fun_tDataMasking_11.setKeepFormat(false);
				fun_tDataMasking_11.setKeepEmpty(false);
				fun_tDataMasking_11.setUseTweak(false);

				class errorMessageStruct_tDataMasking_1Struct {

					public String ERROR_MESSAGE;

					public String getERROR_MESSAGE() {
						return this.ERROR_MESSAGE;
					}

				}
				final errorMessageStruct_tDataMasking_1Struct errorMessage_tDataMasking_1 = new errorMessageStruct_tDataMasking_1Struct();
				org.talend.dataquality.datamasking.DataMasker<row2Struct, row3Struct> duplicator_tDataMasking_1 = new org.talend.dataquality.datamasking.DataMasker<row2Struct, row3Struct>() {
					@Override
					protected row3Struct generateOutput(row2Struct originalStruct, boolean isOriginal) {
						row3Struct tmpStruct = new row3Struct();

						tmpStruct.ID = originalStruct.ID;
						tmpStruct.FirstName = originalStruct.FirstName;
						tmpStruct.LastName = originalStruct.LastName;
						tmpStruct.CRC = originalStruct.CRC;

						if (isOriginal) {
							tmpStruct.ORIGINAL_MARK = true;
						} else {
							modifyOutput(tmpStruct);
							tmpStruct.ORIGINAL_MARK = false;
						}

						return tmpStruct;
					}

					private void modifyOutput(row3Struct row3) {
						Object tmpValue_tDataMasking_1 = null;
						int nbColumns = 0;

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						/* TDQ-18850 msjian: invalid flow, all values should be original values */
						boolean isInputNull_tDataMasking_11 = false;
						boolean isInputEmpty_tDataMasking_11 = false;
						boolean outputNullInMain_tDataMasking_11 = false;
						if (row3.LastName == null) {
							isInputNull_tDataMasking_11 = true;
						} else if ("".equals(row3.LastName.toString().trim())) {
							isInputEmpty_tDataMasking_11 = true;
						}
						// TDQ-21952 if keeNull is true and input is null or keepEmpty is true and input
						// is empty, output to main flow
						if ((true && isInputNull_tDataMasking_11) || (false && isInputEmpty_tDataMasking_11)) {
							tmpValue_tDataMasking_1 = row3.LastName;
						} else {
							tmpValue_tDataMasking_1 = fun_tDataMasking_11.generateMaskedRow(row3.LastName);

							if (tmpValue_tDataMasking_1 == null
									&& !org.talend.dataquality.datamasking.functions.misc.SetToNull.class
											.isInstance(fun_tDataMasking_11)
									|| (org.talend.dataquality.datamasking.functions.email.MaskEmail.class
											.isInstance(fun_tDataMasking_11) && (false || false))) {
								String columnError_tDataMasking_1 = isInputNull_tDataMasking_11 ? "'LastName':null"
										: "LastName";
								errorMessage_tDataMasking_1.ERROR_MESSAGE = errorMessage_tDataMasking_1.ERROR_MESSAGE == null
										? columnError_tDataMasking_1
										: errorMessage_tDataMasking_1.ERROR_MESSAGE + "," + columnError_tDataMasking_1;
							}
							if (tmpValue_tDataMasking_1 == null) {// fix compile error for TDQ-11328
								if (false) {
									row3.LastName = null;
								}

							} else {

								row3.LastName = String.valueOf(tmpValue_tDataMasking_1);
							}
						}

						/* TDQ-18850 msjian: invalid flow, all values should be original values */
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
				 * [tAddCRCRow_1 begin ] start
				 */

				globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.FALSE);

				sh("tAddCRCRow_1");

				s(currentComponent = "tAddCRCRow_1");

				runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, 0, 0, "row1");

				int tos_count_tAddCRCRow_1 = 0;

				if (enableLogStash) {
					talendJobLog.addCM("tAddCRCRow_1", "tAddCRCRow_1", "tAddCRCRow");
					talendJobLogProcess(globalMap);
					s(currentComponent);
				}

				int nb_line_tAddCRCRow_1 = 0;

				/**
				 * [tAddCRCRow_1 begin ] stop
				 */

				/**
				 * [tRowGenerator_1 begin ] start
				 */

				globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.FALSE);

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
					public Integer getRandomID() {

						return Numeric.sequence("s1", 1, 1);

					}

					public String getRandomFirstName() {

						return TalendDataGenerator.getFirstName();

					}

					public String getRandomLastName() {

						return TalendDataGenerator.getLastName();

					}
				}
				tRowGenerator_1Randomizer randtRowGenerator_1 = new tRowGenerator_1Randomizer();

				log.info("tRowGenerator_1 - Generating records.");
				for (int itRowGenerator_1 = 0; itRowGenerator_1 < nb_max_row_tRowGenerator_1; itRowGenerator_1++) {
					row1.ID = randtRowGenerator_1.getRandomID();
					row1.FirstName = randtRowGenerator_1.getRandomFirstName();
					row1.LastName = randtRowGenerator_1.getRandomLastName();
					nb_line_tRowGenerator_1++;

					log.debug("tRowGenerator_1 - Retrieving the record " + nb_line_tRowGenerator_1 + ".");

					/**
					 * [tRowGenerator_1 begin ] stop
					 */

					/**
					 * [tRowGenerator_1 main ] start
					 */

					s(currentComponent = "tRowGenerator_1");

					if (row1 != null) {
						globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.TRUE);
						if (runTrace.isPause()) {
							while (runTrace.isPause()) {
								Thread.sleep(100);
							}
						} else {

							// here we dump the line content for trace purpose
							java.util.LinkedHashMap<String, String> runTraceData = new java.util.LinkedHashMap<String, String>();

							runTraceData.put("ID", String.valueOf(row1.ID));

							runTraceData.put("FirstName", String.valueOf(row1.FirstName));

							runTraceData.put("LastName", String.valueOf(row1.LastName));

							runTrace.sendTrace("row1", "tRowGenerator_1", runTraceData);
						}

					}

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
					 * [tAddCRCRow_1 main ] start
					 */

					s(currentComponent = "tAddCRCRow_1");

					if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

							, "row1", "tRowGenerator_1", "tRowGenerator_1", "tRowGenerator", "tAddCRCRow_1",
							"tAddCRCRow_1", "tAddCRCRow"

					)) {
						talendJobLogProcess(globalMap);
					}

					if (log.isTraceEnabled()) {
						log.trace("row1 - " + (row1 == null ? "" : row1.toLogString()));
					}

					Long crcComputedValuetAddCRCRow_1 = null;
					StringBuilder strBuffer_tAddCRCRow_1 = new StringBuilder();
					strBuffer_tAddCRCRow_1.append(

							String.valueOf(row1.FirstName)

					);

					strBuffer_tAddCRCRow_1.append(

							String.valueOf(row1.LastName)

					);

					java.util.zip.CRC32 crc32tAddCRCRow_1 = new java.util.zip.CRC32();
					crc32tAddCRCRow_1.update(strBuffer_tAddCRCRow_1.toString().getBytes());
					crcComputedValuetAddCRCRow_1 = new Long(crc32tAddCRCRow_1.getValue());
					row2.ID = row1.ID;
					row2.FirstName = row1.FirstName;
					row2.LastName = row1.LastName;
					row2.CRC = crcComputedValuetAddCRCRow_1;
					nb_line_tAddCRCRow_1++;

					if (row2 != null) {
						globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.TRUE);
						if (runTrace.isPause()) {
							while (runTrace.isPause()) {
								Thread.sleep(100);
							}
						} else {

							// here we dump the line content for trace purpose
							java.util.LinkedHashMap<String, String> runTraceData = new java.util.LinkedHashMap<String, String>();

							runTraceData.put("ID", String.valueOf(row2.ID));

							runTraceData.put("FirstName", String.valueOf(row2.FirstName));

							runTraceData.put("LastName", String.valueOf(row2.LastName));

							runTraceData.put("CRC", String.valueOf(row2.CRC));

							runTrace.sendTrace("row2", "tRowGenerator_1", runTraceData);
						}

					}

					tos_count_tAddCRCRow_1++;

					/**
					 * [tAddCRCRow_1 main ] stop
					 */

					/**
					 * [tAddCRCRow_1 process_data_begin ] start
					 */

					s(currentComponent = "tAddCRCRow_1");

					/**
					 * [tAddCRCRow_1 process_data_begin ] stop
					 */

					/**
					 * [tDataMasking_1 main ] start
					 */

					s(currentComponent = "tDataMasking_1");

					if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

							, "row2", "tAddCRCRow_1", "tAddCRCRow_1", "tAddCRCRow", "tDataMasking_1", "tDataMasking_1",
							"tDataMasking"

					)) {
						talendJobLogProcess(globalMap);
					}

					if (log.isTraceEnabled()) {
						log.trace("row2 - " + (row2 == null ? "" : row2.toLogString()));
					}

					errorMessage_tDataMasking_1.ERROR_MESSAGE = null;
					List<row3Struct> row3ReslutList = duplicator_tDataMasking_1.process(row2, false);

					for (row3Struct tmpStructMask_tDataMasking_1 : row3ReslutList) {
						if (errorMessage_tDataMasking_1 == null || errorMessage_tDataMasking_1.ERROR_MESSAGE == null) {
							row3 = tmpStructMask_tDataMasking_1;
						} else {
							row3 = null;
						}

						if (row3 != null) {
							globalMap.put("ENABLE_TRACES_CONNECTION_tRowGenerator_1", Boolean.TRUE);
							if (runTrace.isPause()) {
								while (runTrace.isPause()) {
									Thread.sleep(100);
								}
							} else {

								// here we dump the line content for trace purpose
								java.util.LinkedHashMap<String, String> runTraceData = new java.util.LinkedHashMap<String, String>();

								runTraceData.put("ID", String.valueOf(row3.ID));

								runTraceData.put("FirstName", String.valueOf(row3.FirstName));

								runTraceData.put("LastName", String.valueOf(row3.LastName));

								runTraceData.put("CRC", String.valueOf(row3.CRC));

								runTraceData.put("ORIGINAL_MARK", String.valueOf(row3.ORIGINAL_MARK));

								runTrace.sendTrace("row3", "tRowGenerator_1", runTraceData);
							}

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

// Start of branch "row3"
						if (row3 != null) {

							/**
							 * [tLogRow_1 main ] start
							 */

							s(currentComponent = "tLogRow_1");

							if (runStat.update(execStat, enableLogStash, iterateId, 1, 1

									, "row3", "tDataMasking_1", "tDataMasking_1", "tDataMasking", "tLogRow_1",
									"tLogRow_1", "tLogRow"

							)) {
								talendJobLogProcess(globalMap);
							}

							if (log.isTraceEnabled()) {
								log.trace("row3 - " + (row3 == null ? "" : row3.toLogString()));
							}

///////////////////////		

							String[] row_tLogRow_1 = new String[5];

							if (row3.ID != null) { //
								row_tLogRow_1[0] = String.valueOf(row3.ID);

							} //

							if (row3.FirstName != null) { //
								row_tLogRow_1[1] = String.valueOf(row3.FirstName);

							} //

							if (row3.LastName != null) { //
								row_tLogRow_1[2] = String.valueOf(row3.LastName);

							} //

							if (row3.CRC != null) { //
								row_tLogRow_1[3] = String.valueOf(row3.CRC);

							} //

							if (row3.ORIGINAL_MARK != null) { //
								row_tLogRow_1[4] = String.valueOf(row3.ORIGINAL_MARK);

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

						} // End of branch "row3"

						// end for
					}

					/**
					 * [tDataMasking_1 process_data_end ] start
					 */

					s(currentComponent = "tDataMasking_1");

					/**
					 * [tDataMasking_1 process_data_end ] stop
					 */

					/**
					 * [tAddCRCRow_1 process_data_end ] start
					 */

					s(currentComponent = "tAddCRCRow_1");

					/**
					 * [tAddCRCRow_1 process_data_end ] stop
					 */

					/**
					 * [tRowGenerator_1 process_data_end ] start
					 */

					s(currentComponent = "tRowGenerator_1");

					/**
					 * [tRowGenerator_1 process_data_end ] stop
					 */

					if (!isChildJob && (Boolean) globalMap.get("ENABLE_TRACES_CONNECTION_tRowGenerator_1")) {
						if (globalMap.get("USE_CONDITION") != null && (Boolean) globalMap.get("USE_CONDITION")) {
							if (globalMap.get("TRACE_CONDITION") != null
									&& (Boolean) globalMap.get("TRACE_CONDITION")) {
								// if next breakpoint has been clicked on UI or if start job, should wait action
								// of user.
								if (runTrace.isNextBreakpoint()) {
									runTrace.waitForUserAction();
								} else if (runTrace.isNextRow()) {
									runTrace.waitForUserAction();
								}
							} else {
								// if next row has been clicked on UI or if start job, should wait action of
								// user.
								if (runTrace.isNextRow()) {
									runTrace.waitForUserAction();
								}
							}
						} else { // no condition set
							if (runTrace.isNextRow()) {
								runTrace.waitForUserAction();
							} else {
								Thread.sleep(1000);
							}
						}

					}
					globalMap.put("USE_CONDITION", Boolean.FALSE);

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
				 * [tAddCRCRow_1 end ] start
				 */

				s(currentComponent = "tAddCRCRow_1");

				globalMap.put("tAddCRCRow_1_NB_LINE", nb_line_tAddCRCRow_1);

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row1", 2, 0,
						"tRowGenerator_1", "tRowGenerator_1", "tRowGenerator", "tAddCRCRow_1", "tAddCRCRow_1",
						"tAddCRCRow", "output")) {
					talendJobLogProcess(globalMap);
				}

				ok_Hash.put("tAddCRCRow_1", true);
				end_Hash.put("tAddCRCRow_1", System.currentTimeMillis());

				/**
				 * [tAddCRCRow_1 end ] stop
				 */

				/**
				 * [tDataMasking_1 end ] start
				 */

				s(currentComponent = "tDataMasking_1");

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row2", 2, 0,
						"tAddCRCRow_1", "tAddCRCRow_1", "tAddCRCRow", "tDataMasking_1", "tDataMasking_1",
						"tDataMasking", "output")) {
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

				if (runStat.updateStatAndLog(execStat, enableLogStash, resourceMap, iterateId, "row3", 2, 0,
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
				 * [tAddCRCRow_1 finally ] start
				 */

				s(currentComponent = "tAddCRCRow_1");

				/**
				 * [tAddCRCRow_1 finally ] stop
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

		globalMap.put("tRowGenerator_1_SUBPROCESS_STATE", 1);
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

				globalMap.put("ENABLE_TRACES_CONNECTION_talendJobLog", Boolean.FALSE);

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

				if (!isChildJob && (Boolean) globalMap.get("ENABLE_TRACES_CONNECTION_talendJobLog")) {
					if (globalMap.get("USE_CONDITION") != null && (Boolean) globalMap.get("USE_CONDITION")) {
						if (globalMap.get("TRACE_CONDITION") != null && (Boolean) globalMap.get("TRACE_CONDITION")) {
							// if next breakpoint has been clicked on UI or if start job, should wait action
							// of user.
							if (runTrace.isNextBreakpoint()) {
								runTrace.waitForUserAction();
							} else if (runTrace.isNextRow()) {
								runTrace.waitForUserAction();
							}
						} else {
							// if next row has been clicked on UI or if start job, should wait action of
							// user.
							if (runTrace.isNextRow()) {
								runTrace.waitForUserAction();
							}
						}
					} else { // no condition set
						if (runTrace.isNextRow()) {
							runTrace.waitForUserAction();
						} else {
							Thread.sleep(1000);
						}
					}

				}
				globalMap.put("USE_CONDITION", Boolean.FALSE);

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
		final data_hashing data_hashingClass = new data_hashing();

		int exitCode = data_hashingClass.runJobInTOS(args);
		if (exitCode == 0) {
			log.info("TalendJob: 'data_hashing' - Done.");
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
		log.info("TalendJob: 'data_hashing' - Start.");

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
		org.slf4j.MDC.put("_jobRepositoryId", "_tu7SMKXAEeqL4ZI8TPRa0w");
		org.slf4j.MDC.put("_compiledAtTimestamp", "2025-06-30T21:55:01.228933200Z");

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
			java.io.InputStream inContext = data_hashing.class.getClassLoader()
					.getResourceAsStream("dataprivacy/data_hashing_0_1/contexts/" + contextStr + ".properties");
			if (inContext == null) {
				inContext = data_hashing.class.getClassLoader()
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
		log.info("TalendJob: 'data_hashing' - Started.");
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

		try {
			runTrace.openSocket(!isChildJob);
			runTrace.startThreadTrace(clientHost, portTraces);
			if (runTrace.isPause()) {
				while (runTrace.isPause()) {
					Thread.sleep(100);
				}
			}
		} catch (java.io.IOException ioException) {
			ioException.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
			System.out
					.println((endUsedMemory - startUsedMemory) + " bytes memory increase when running : data_hashing");
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
		runTrace.stopThreadTrace();
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
		log.info("TalendJob: 'data_hashing' - Finished - status: " + status + " returnCode: " + returnCode);

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
 * 119643 characters generated by Talend Cloud Data Management Platform on the
 * June 30, 2025 at 10:55:01 PM BST
 ************************************************************************************************/