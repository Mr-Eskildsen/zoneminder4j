package name.eskildsen.zoneminder.api.config;

public enum ZoneMinderConfigEnum {
	
	/**
	 * 
	 * Display Page
	 * 
	 */
	ZM_SKIN_DEFAULT, //	Default skin used by web interface (?)	
	//classic
	ZM_CSS_DEFAULT, //	Default set of css files used by web interface (?)	 classic   dark   flat
	ZM_LANG_DEFAULT, //	Default language used by web interface (?)	
	ZM_OPT_USE_AUTH, //	Authenticate user logins to ZoneMinder (?)	
	ZM_AUTH_TYPE, //	What is used to authenticate ZoneMinder users (?)	 builtin   remote
	ZM_AUTH_RELAY, //	Method used to relay authentication information (?)	 hashed   plain   none
	ZM_AUTH_HASH_SECRET, //	Secret for encoding hashed authentication information (?)	
	//...Change me to something unique...
	ZM_AUTH_HASH_IPS, //	Include IP addresses in the authentication hash (?)	
	ZM_AUTH_HASH_LOGINS, //	Allow login by authentication hash (?)	
	ZM_OPT_USE_API, //	Enable ZoneMinder APIs (?)	
	ZM_OPT_USE_GOOG_RECAPTCHA, //	Add Google reCaptcha to login page (?)	
	ZM_OPT_GOOG_RECAPTCHA_SITEKEY, //	Your recaptcha site-key (?)	
	//...Insert your recaptcha site-key here...
	ZM_OPT_GOOG_RECAPTCHA_SECRETKEY, //	Your recaptcha secret-key (?)	
	//...Insert your recaptcha secret-key here...
	ZM_OPT_FAST_DELETE, //	Delete only event database records for speed (?)	
	ZM_FILTER_RELOAD_DELAY, //	How often (in seconds) filters are reloaded in zmfilter (?)	
	//300
	ZM_FILTER_EXECUTE_INTERVAL, //	How often (in seconds) to run automatic saved filters (?)	
	//60
	ZM_MAX_RESTART_DELAY, //	Maximum delay (in seconds) for daemon restart attempts. (?)	
	//600
	ZM_WATCH_CHECK_INTERVAL, //	How often to check the capture daemons have not locked up (?)	
	//10
	ZM_WATCH_MAX_DELAY, //	The maximum delay allowed since the last captured image (?)	
	//5
	ZM_RUN_AUDIT, //	Run zmaudit to check data consistency (?)	
	ZM_AUDIT_CHECK_INTERVAL, //	How often to check database and filesystem consistency (?)	
	//900
	ZM_AUDIT_MIN_AGE, //	The minimum age in seconds event data must be in order to be deleted. (?)	
	//86400
	ZM_OPT_FRAME_SERVER, //	Should analysis farm out the writing of images to disk (?)	
	ZM_FRAME_SOCKET_SIZE, //	Specify the frame server socket buffer size if non-standard (?)
	//0
	ZM_OPT_CONTROL, //	Support controllable (e.g. PTZ) cameras (?)	
	ZM_OPT_TRIGGERS, //	Interface external event triggers via socket or device files (?)	
	ZM_CHECK_FOR_UPDATES, //	Check with zoneminder.com for updated versions (?)	
	ZM_TELEMETRY_DATA, //	Send usage information to ZoneMinder (?)	
	ZM_UPDATE_CHECK_PROXY, //	Proxy url if required to access zoneminder.com (?)	
	ZM_SHM_KEY, //Shared memory root key to use (?)
    //ZM_OPT_USE_API,
    //ZM_OPT_FRAME_SERVER,
    //ZM_OPT_TRIGGERS;
	
	
	
	/**
	 * 
	 * System Page
	 * 
	 */
	ZM_TIMESTAMP_ON_CAPTURE, //	Timestamp images as soon as they are captured (?)	
	ZM_CPU_EXTENSIONS, //	Use advanced CPU extensions to increase performance (?)	
	ZM_FAST_IMAGE_BLENDS, //	Use a fast algorithm to blend the reference image (?)	
	ZM_OPT_ADAPTIVE_SKIP, //	Should frame analysis try and be efficient in skipping frames (?)	
	ZM_MAX_SUSPEND_TIME, //	Maximum time that a monitor may have motion detection suspended (?)	
	//30
	ZM_STRICT_VIDEO_CONFIG, //	Allow errors in setting video config to be fatal (?)	
	ZM_LD_PRELOAD, //	Path to library to preload before launching daemons (?)	
	ZM_SIGNAL_CHECK_POINTS, //	How many points in a captured image to check for signal loss (?)	
	//10
	ZM_V4L_MULTI_BUFFER, //	Use more than one buffer for Video 4 Linux devices (?)	
	CAPTURES_PER_FRAME, //	How many images are captured per returned frame, for shared local cameras (?)	
	//1
	ZM_FORCED_ALARM_SCORE, //	Score to give forced alarms (?)	
	//255
	ZM_BULK_FRAME_INTERVAL, //	How often a bulk frame should be written to the database (?)	
	//100
	ZM_EVENT_CLOSE_MODE, //	When continuous events are closed. (?)	 time   idle   alarm
	ZM_CREATE_ANALYSIS_IMAGES, //	Create analysed alarm images with motion outlined (?)	
	ZM_WEIGHTED_ALARM_CENTRES, //	Use a weighted algorithm to calculate the centre of an alarm (?)	
	ZM_EVENT_IMAGE_DIGITS, //	How many significant digits are used in event image numbering (?)	
	//5
	ZM_DEFAULT_ASPECT_RATIO, //	The default width:height aspect ratio used in monitors (?)	
	//4:3
	ZM_USER_SELF_EDIT,	//	Allow unprivileged users to change their details (?)
	
	
	
	
	/**
	 * 
	 * Path Page
	 * 
	 */
	ZM_DIR_EVENTS, //	Directory where events are stored (?)	
	//events
	ZM_DIR_IMAGES, //	Directory where the images that the ZoneMinder client generates are stored (?)	
	//images
	ZM_DIR_SOUNDS, //	Directory to the sounds that the ZoneMinder client can use (?)	
	//sounds
	ZM_PATH_ZMS, //	Web path to zms streaming server (?)	
	///zm/cgi-bin/nph-zms
	ZM_PATH_MAP, //	Path to the mapped memory files that that ZoneMinder can use (?)	
	///dev/shm
	ZM_PATH_SOCKS, //	Path to the various Unix domain socket files that ZoneMinder uses (?)	
	///var/run/zm
	ZM_PATH_LOGS, //	Path to the various logs that the ZoneMinder daemons generate (?)	
	///var/log/zm
	ZM_PATH_SWAP, //	Path to location for temporary swap images used in streaming (?)	
	///tmp/zm
	ZM_PATH_ARP, //
	
	
	
	/**
	 * 
	 * WEB Tab
	 * 
	 */
	ZM_WEB_TITLE_PREFIX, //	The title prefix displayed on each window (?)	
	//ZM
	ZM_WEB_RESIZE_CONSOLE, //	Should the console window resize itself to fit (?)	
	ZM_WEB_POPUP_ON_ALARM, //	Should the monitor window jump to the top if an alarm occurs (?)	
	ZM_WEB_SOUND_ON_ALARM, //	Should the monitor window play a sound if an alarm occurs (?)	
	ZM_WEB_ALARM_SOUND, //	The sound to play on alarm, put this in the sounds directory (?)	
	ZM_WEB_COMPACT_MONTAGE, //	Compact the montage view by removing extra detail (?)	
	ZM_WEB_EVENT_SORT_FIELD, //	Default field the event lists are sorted by (?)	
	ZM_WEB_EVENT_SORT_ORDER, //	Default order the event lists are sorted by (?)	 asc   desc
	ZM_WEB_EVENTS_PER_PAGE, //	How many events to list per page in paged mode (?)	
	//25
	ZM_WEB_LIST_THUMBS, //	Display mini-thumbnails of event images in event lists (?)	
	ZM_WEB_LIST_THUMB_WIDTH, //	The width of the thumbnails that appear in the event lists (?)	
	//48
	ZM_WEB_LIST_THUMB_HEIGHT, //	The height of the thumbnails that appear in the event lists (?)	
	//0
	ZM_WEB_USE_OBJECT_TAGS, //	Wrap embed in object tags for media content (?)
	
	/**
	 * 
	 * IMAGES Tab
	 * 
	 */
	
	ZM_COLOUR_JPEG_FILES, //	Colourise greyscale JPEG files (?)	
	ZM_ADD_JPEG_COMMENTS, //	Add jpeg timestamp annotations as file header comments (?)	
	ZM_JPEG_FILE_QUALITY, //	Set the JPEG quality setting for the saved event files (1-100) (?)	
	//70
	ZM_JPEG_ALARM_FILE_QUALITY, //	Set the JPEG quality setting for the saved event files during an alarm (1-100) (?)	
	//0
	ZM_JPEG_STREAM_QUALITY, //	Set the JPEG quality setting for the streamed 'live' images (1-100) (?)	
	//70
	ZM_MPEG_TIMED_FRAMES, //	Tag video frames with a timestamp for more realistic streaming (?)	
	ZM_MPEG_LIVE_FORMAT, //	What format 'live' video streams are played in (?)	
	//swf
	ZM_MPEG_REPLAY_FORMAT, //	What format 'replay' video streams are played in (?)	
	//swf
	ZM_RAND_STREAM, //	Add a random string to prevent caching of streams (?)	
	ZM_OPT_CAMBOZOLA, //	Is the (optional) cambozola java streaming client installed (?)	
	ZM_PATH_CAMBOZOLA, //	Web path to (optional) cambozola java streaming client (?)	
	//cambozola.jar
	ZM_RELOAD_CAMBOZOLA, //	After how many seconds should Cambozola be reloaded in live view (?)	
	//0
	ZM_OPT_FFMPEG, //	Is the ffmpeg video encoder/decoder installed (?)	
	ZM_PATH_FFMPEG, //	Path to (optional) ffmpeg mpeg encoder (?)	
	ZM_FFMPEG_INPUT_OPTIONS, //	Additional input options to ffmpeg (?)	
	ZM_FFMPEG_OUTPUT_OPTIONS, //	Additional output options to ffmpeg (?)	
	//-r 25
	ZM_FFMPEG_FORMATS, //	Formats to allow for ffmpeg video generation (?)	
	//mpg mpeg wmv asf avi* mov swf 3gp**
	ZM_FFMPEG_OPEN_TIMEOUT, //	Timeout in seconds when opening a stream. (?)
	
	
	
	
	/**
	 * 
	 * LOGGING Tab
	 * 
	 */
	
	ZM_LOG_LEVEL_SYSLOG, //	Save logging output to the system log (?)	
	ZM_LOG_LEVEL_FILE, //	Save logging output to component files (?)	
	ZM_LOG_LEVEL_WEBLOG, //	Save logging output to the weblog (?)	
	ZM_LOG_LEVEL_DATABASE, //	Save logging output to the database (?)	
	ZM_LOG_DATABASE_LIMIT, //	Maximum number of log entries to retain (?)	
	//7 day
	ZM_LOG_DEBUG, //	Switch debugging on (?)	
	ZM_LOG_DEBUG_TARGET, //	What components should have extra debug enabled (?)	
	ZM_LOG_DEBUG_LEVEL, //	What level of extra debug should be enabled (?)	
	ZM_LOG_DEBUG_FILE, //	Where extra debug is output to (?)	
	// /var/log/zm/zm_debug.log+
	ZM_LOG_CHECK_PERIOD, //	Time period used when calculating overall system health (?)	
	//900
	ZM_LOG_ALERT_WAR_COUNT, //	Number of warnings indicating system alert state (?)	
	//1
	ZM_LOG_ALERT_ERR_COUNT, //	Number of errors indicating system alert state (?)	
	//1
	ZM_LOG_ALERT_FAT_COUNT, //	Number of fatal error indicating system alert state (?)	
	//0
	ZM_LOG_ALARM_WAR_COUNT, //	Number of warnings indicating system alarm state (?)	
	//100
	ZM_LOG_ALARM_ERR_COUNT, //	Number of errors indicating system alarm state (?)	
	//10
	ZM_LOG_ALARM_FAT_COUNT, //	Number of fatal error indicating system alarm state (?)	
	//1
	ZM_RECORD_EVENT_STATS, //	Record event statistical information, switch off if too slow (?)	
	ZM_RECORD_DIAG_IMAGES, //	Record intermediate alarm diagnostic images, can be very slow (?)	
	ZM_DUMP_CORE, //S	Create core files on unexpected process failure. (?)
	

	/**
	 * 
	 * Network Tab
	 * 
	 */
	
	
	ZM_HTTP_VERSION, //	The version of HTTP that ZoneMinder will use to connect (?)	 1.1   1.0
	ZM_HTTP_UA, //	The user agent that ZoneMinder uses to identify itself (?)	
	//ZoneMinder
	ZM_HTTP_TIMEOUT, //	How long ZoneMinder waits before giving up on images (milliseconds) (?)	
	//2500
	ZM_MIN_RTP_PORT, //	Minimum port that ZoneMinder will listen for RTP traffic on (?)	
	//40200
	ZM_MAX_RTP_PORT, //	Maximum port that ZoneMinder will listen for RTP traffic on (?)
}
