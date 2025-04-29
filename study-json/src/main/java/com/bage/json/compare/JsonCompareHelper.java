package com.bage.json.compare;

import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.ValueObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonCompareHelper {
    /**
     * 如果存在差异， 返回 list 为空，， 否则 返回 差异的字段路径     *     * @param str1     * @param str2     * @return
     */
    public static CompareDiffItem diffDetail(String str1, String str2) {
        try {
            // given
            Javers javers = JaversBuilder.javers().build();
            // when
            Diff diff = javers.compare(JsonUtil.toJsonNode(str1), JsonUtil.toJsonNode(str2));
            // then
            List<String> pathList = diff.getChanges().stream()
                    .map(change -> mapping(change))
                    .map(change -> replaceSomeField(change))
                    .filter(change -> filterByConfig(change))
                    .collect(Collectors.toList());
            return CompareDiffItem.builder()
                    .summary(subIfNecessary(replaceSomeField(diff.prettyPrint())))
                    .paths(pathList)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return CompareDiffItem.builder()
                .paths(new ArrayList<>())
                .build();
    }

    private static String subIfNecessary(String str) {
        if (str == null) {
            return "";
        }
        String maxLength = QConfigUtil.getString("auto.test.compare.summary.max.length");
        if (maxLength == null || maxLength.isEmpty()) {
            maxLength = "1800";
        }

        int parsed = Integer.parseInt(maxLength);
        if (str.length() >= parsed) {
            return str.substring(0, parsed) + "...";
        }
        return str;
    }

    private static boolean filterByConfig(String path) {
        String ignoreFields = QConfigUtil.getString("auto.test.compare.ignore.fields");
        if (ignoreFields == null) {
            return true;
        }
        String[] split = ignoreFields.split(",");
        for (String str : split) {
            if (Pattern.compile(str).matcher(path).matches()) {
                log.info("skip by config, config = {}, path = {}", str, path);
                return false;

            }
        }
        return true;
    }

    private static String mapping(Change change) {
        if (change.getAffectedGlobalId() instanceof ValueObjectId) {
            String fragment = ((ValueObjectId) (change.getAffectedGlobalId())).getFragment();
            return fragment;
        }
        if (change instanceof ValueChange) {
            String propertyName = ((ValueChange) change).getPropertyName();
            return propertyName;
        }
        return "unknown-unsupported";
    }

    private static String replaceSomeField(String originPath) {
        // _children/RequestOrder/_children/BackExt => RequestOrder/BackExt
        // 2. ValueChange{globalId:'com.fasterxml.jackson.databind.node.ObjectNode/#_children/RequestOrder/_children/BackExt', property:'_value', oldVal:'{"grabDefaultPackagePrice":40,"rnVersion":2.025042401E7,"mainRnVersion":2.025042401E7,"zlRnVersion":2.024090401E7,"preSaleListV2":[{"preSaleDateTime":"20250421153000","departDate":"20250505"},{"preSaleDateTime":"20250420153000","departDate":"20250504"}],"redeemAlertContent":"","originalTicketInfoV2":{"departStation":"牡丹江","arriveStation":"北京"},"grabProcessType":1,"ticketReimbursePassengerImgKeyList":[]}', newVal:'{"grabDefaultPacddkagePrice":40,"rnVersion":2.025042401E7,"mainRnVersion":2.025042401E7,"zlRnVersion":2.024090401E7,"preSaleListV2":[{"preSaleDateTime":"20250421153000","departDate":"20250505"},{"preSaleDateTime":"20250420153000","departDate":"20250504"}],"redeemAlertContent":"","originalTicketInfoV2":{"departStation":"牡丹江","arriveStation":"北京"},"grabProcessType":1,"ticketReimbursePassengerImgKeyList":[]}'}
        return "/" + originPath.replace("_children/", "")
                .replace("com.fasterxml.jackson.databind.node.ObjectNode/#", "");
    }
}
