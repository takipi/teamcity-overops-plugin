package com.overops.plugins.runner.runner;

import com.overops.plugins.Constants;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.util.*;

public class OverOpsRunType extends RunType {

    public OverOpsRunType(RunTypeRegistry runTypeRegistry) {
        runTypeRegistry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return Constants.RUNNER_TYPE;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return Constants.PLUGIN_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return Constants.PLUGIN_DESCRIPTION;
    }

    @Nullable
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor() {
        return new PropertiesProcessor() {
            @Override
            public Collection<InvalidProperty> process(Map<String, String> map) {
                List<InvalidProperty> result = new ArrayList<InvalidProperty>();
                if(StringUtils.isEmpty(map.get("url"))) {
                    result.add(new InvalidProperty("url", "OverOpsURL is required."));
                }
                if(StringUtils.isEmpty(map.get("envId"))) {
                    result.add(new InvalidProperty("envId", "OverOps Environment ID."));
                }
                if(StringUtils.isEmpty(map.get("token"))) {
                    result.add(new InvalidProperty("token", "OverOps API Token."));
                }
                return result;
            }
        };
    }

    @Nullable
    @Override
    public String getEditRunnerParamsJspFilePath() {
        return Constants.EDIT_PROPS;
    }

    @Nullable
    @Override
    public String getViewRunnerParamsJspFilePath() {
        //TODO here will be view page
        return null;
    }

    @Nullable
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.APP_NAME, Constants.DEFAULT_APP_NAME);
        map.put(Constants.DEPLOYMENT_NAME, Constants.DEFAULT_DEPLOYMENT_NAME);
        map.put(Constants.FIELD_CHECK_URL, Constants.DEFAULT_URL);
        map.put(Constants.FIELD_CHECK_NEW_ERROR, Constants.DEFAULT_CHECK_NEW_ERROR);
        map.put(Constants.FIELD_CHECK_RESURFACED_ERRORS, Constants.DEFAULT_CHECK_RESURFACED_ERRORS);
        map.put(Constants.FIELD_VOLUME_ERRORS, Constants.DEFAULT_VOLUME_ERRORS);
        map.put(Constants.FIELD_UNIQUE_ERRORS, Constants.DEFAULT_UNIQUE_ERRORS);
        map.put(Constants.FIELD_CRITICAL_ERRORS, Constants.DEFAULT_CRITICAL_ERRORS);
        map.put(Constants.FIELD_MARK_UNSTABLE, Constants.DEFAULT_MARK_UNSTABLE);
        map.put(Constants.FIELD_LINK, Constants.DEFAULT_LINK);
        map.put(Constants.FIELD_PRINT_TOP_ISSUE, Constants.DEFAULT_PRINT_TOP_ISSUE);

        map.put(Constants.FIELD_MAX_ERROR_VOLUME, Constants.DEFAULT_MAX_ERROR_VOLUME);
        map.put(Constants.FIELD_MAX_UNIQUE_ERRORS, Constants.DEFAULT_MAX_UNIQUE_ERRORS);
        map.put(Constants.FIELD_MIN_VOLUME_THRESHOLD, Constants.DEFAULT_MIN_VOLUME_THRESHOLD);
        map.put(Constants.FIELD_DEBUG, Constants.DEFAULT_DEBUG);
        return map;
    }

    @NotNull
    @Override
    public String describeParameters(@NotNull Map<String, String> parameters) {
        // LinkedHashMap preserves insertion order
        Map<String,String> qualityGates = new LinkedHashMap<String,String>();

        qualityGates.put("Total", parameters.get(Constants.FIELD_VOLUME_ERRORS));
        qualityGates.put("Unique", parameters.get(Constants.FIELD_UNIQUE_ERRORS));
        qualityGates.put("Critical", parameters.get(Constants.FIELD_CRITICAL_ERRORS));

        StringBuilder sb = new StringBuilder("Quality Gates: ");
        String separator = ", ";

        qualityGates.forEach((k,v) -> {
          if(!StringUtils.isEmpty(v) && v.equals("true")) {
            sb.append(k);
            sb.append(separator);
          }
        });

        if (!sb.substring(sb.length() - 2, sb.length()).equals(separator)) {
          // no gates enabled
          sb.append("None");
        } else {
          // remove trailing separator
          sb.replace(sb.length() - 2, sb.length(), "");
        }

        return sb.toString();
      }
}
