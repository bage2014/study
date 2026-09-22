package com.bage.study.ai.best.practice.rca.agent.scan;

import com.bage.study.ai.best.practice.rca.agent.dto.RcaRequest;
import com.bage.study.ai.best.practice.rca.agent.model.Evidence;
import com.bage.study.ai.best.practice.rca.agent.model.TimelineEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 时间线重建（rca.md P0 项）：告警 / 变更 / 异常信号强制按统一时间戳排列，
 * 让"变更 → 信号 → 告警"的时序因果显式暴露。
 */
@Service
public class TimelineService {

    public List<TimelineEvent> build(RcaRequest request, List<Evidence> evidences,
                                     List<TimelineEvent> changeEvents) {
        List<TimelineEvent> timeline = new ArrayList<>();

        timeline.add(new TimelineEvent(
                request.getAlarmTime(), TimelineEvent.Kind.ALERT,
                "alarm", request.getAppId(),
                "告警触发：" + request.getAlarmDescription()));

        timeline.addAll(changeEvents);

        for (Evidence e : evidences) {
            if (e.isAnomaly()) {
                timeline.add(new TimelineEvent(
                        e.getObservedAt(), TimelineEvent.Kind.SIGNAL,
                        e.getToolName(), e.getTargetName(),
                        String.format("异常信号：%s 偏离基线 %.2fx", e.getToolName(), e.getDeviationRatio())));
            }
        }

        timeline.sort(Comparator.comparing(TimelineEvent::time)
                .thenComparing(t -> t.kind().name()));
        return timeline;
    }

    /**
     * 变更感知：在参考时间点（最早异常信号，缺省用告警时间）前 windowMinutes 内，
     * 查找与假设目标同名的变更事件。
     */
    public TimelineEvent findTriggerChange(List<TimelineEvent> changeEvents, String targetName,
                                           LocalDateTime earliestSignal, LocalDateTime alarmTime,
                                           int windowMinutes) {
        LocalDateTime anchor = earliestSignal != null ? earliestSignal : alarmTime;
        if (anchor == null) {
            return null;
        }
        LocalDateTime windowStart = anchor.minusMinutes(windowMinutes);
        return changeEvents.stream()
                .filter(e -> targetName.equals(e.relatedTarget()))
                .filter(e -> !e.time().isAfter(anchor) && !e.time().isBefore(windowStart))
                .max(Comparator.comparing(TimelineEvent::time))
                .orElse(null);
    }
}
