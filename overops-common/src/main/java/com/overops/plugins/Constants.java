package com.overops.plugins;

public class Constants {

    public static final String EDIT_PROPS = "editOverOpsRunParams.jsp";
    public static final String RUNNER_TYPE = "overops-plugin";
    public static final String PLUGIN_NAME = "OverOps";
    public static final String PLUGIN_DESCRIPTION = "Generate OverOps Quality Report";

    public static final String SETTING_APP_HOST = "url";
    public static final String SETTING_ENV_ID = "envId";
    public static final String SETTING_API_TOKEN = "token";

    public static final String OVER_OPS_MANAGER_KEY = "overops";

    public static final String APP_NAME = "applicationName";
    public static final String DEPLOYMENT_NAME = "deploymentName";

    public static final String FIELD_URL = "overops.url";
    public static final String FIELD_TOKEN = "overops.token";
    public static final String FIELD_ENV_ID = "overops.env.id";
    public static final String FIELD_PROJECT_ID = "projectId";
    public static final String FIELD_TESTING = "testing";

    public static final String FIELD_CHECK_URL = "url";
    public static final String FIELD_CHECK_NEW_ERROR = "checkNewErrors";
    public static final String FIELD_CHECK_RESURFACED_ERRORS = "checkResurfacedErrors";
    public static final String FIELD_VOLUME_ERRORS = "checkVolumeErrors";
    public static final String FIELD_UNIQUE_ERRORS = "checkUniqueErrors";
    public static final String FIELD_CRITICAL_ERRORS = "checkCriticalErrors";
    public static final String FIELD_REGRESSIONS_ERROR = "checkRegressionErrors";
    public static final String FIELD_MARK_UNSTABLE = "markUnstable";
    public static final String FIELD_PRINT_TOP_ISSUE = "printTopIssues";
    public static final String FIELD_NEW_EVENTS = "newEvents";
    public static final String FIELD_RESURFACED_ERRORS = "resurfacedErrors";
    public static final String FIELD_MAX_ERROR_VOLUME = "maxErrorVolume";
    public static final String FIELD_MAX_UNIQUE_ERRORS = "maxUniqueErrors";
    public static final String FIELD_MIN_VOLUME_THRESHOLD = "minVolumeThreshold";
    public static final String FIELD_MIN_ERROR_RATE_THRESHOLD = "minErrorRateThreshold";
    public static final String FIELD_REGRESSION_DELTA = "regressionDelta";
    public static final String FIELD_CRITICAL_REGRESSION_DELTA = "criticalRegressionDelta";
    public static final String FIELD_APPLY_SEASONALITY = "applySeasonality";
    public static final String FIELD_DEBUG = "debug";

    public static final String DEFAULT_APP_NAME = "%system.teamcity.projectName%";
    public static final String DEFAULT_DEPLOYMENT_NAME = "%build.number%";
    public static final String DEFAULT_URL = "https://api.overops.com";
    public static final String DEFAULT_CHECK_NEW_ERROR = "true";
    public static final String DEFAULT_CHECK_RESURFACED_ERRORS = "true";
    public static final String DEFAULT_VOLUME_ERRORS = "true";
    public static final String DEFAULT_UNIQUE_ERRORS = "true";
    public static final String DEFAULT_CRITICAL_ERRORS = "true";
    public static final String DEFAULT_REGRESSIONS_ERROR = "true";
    public static final String DEFAULT_MARK_UNSTABLE = "false";
    public static final String DEFAULT_PRINT_TOP_ISSUE = "5";
    public static final String DEFAULT_NEW_EVENTS = "false";
    public static final String DEFAULT_RESURFACED_ERRORS = "false";
    public static final String DEFAULT_MAX_ERROR_VOLUME = "0";
    public static final String DEFAULT_MAX_UNIQUE_ERRORS = "0";
    public static final String DEFAULT_MIN_VOLUME_THRESHOLD = "0";
    public static final String DEFAULT_MIN_ERROR_RATE_THRESHOLD = "0";
    public static final String DEFAULT_REGRESSION_DELTA = "0";
    public static final String DEFAULT_CRITICAL_REGRESSION_DELTA = "0";
    public static final String DEFAULT_APPLY_SEASONALITY = "false";
    public static final String DEFAULT_DEBUG = "false";

    public static final String RUNNER_DISPLAY_NAME = "OverOps";
    public static final String OV_REPORTS_FILE = "OverOpsReport.json";
    public static final String OV_REPORTS_FILE_RESULT = "OverOpsReportResult.json";
}
