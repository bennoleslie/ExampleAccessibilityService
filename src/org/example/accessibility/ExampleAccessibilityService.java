package org.example.accessibility;

import java.util.List;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityWindowInfo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;

public class ExampleAccessibilityService extends AccessibilityService {

    static final String TAG = "ExampleAccessibilityService";

    private String getEventTypeString(int eventType) {
        switch (eventType) {
        case AccessibilityEvent.TYPE_ANNOUNCEMENT:
            return "TYPE_ANNOUNCEMENT";
        case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
            return "TYPE_GESTURE_DETECTION_END";
        case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
            return "TYPE_GESTURE_DETECTION_START";
        case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
            return "TYPE_NOTIFICATION_STATE_CHANGED";
        case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
            return "TYPE_TOUCH_EXPLORATION_GESTURE_END";
        case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
            return "TYPE_TOUCH_EXPLORATION_GESTURE_START";
        case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
            return "TYPE_TOUCH_INTERACTION_END";
        case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
            return "TYPE_TOUCH_INTERACTION_START";
        case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
            return "TYPE_VIEW_ACCESSIBILITY_FOCUSED";
        case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
            return "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";
        case AccessibilityEvent.TYPE_VIEW_CLICKED:
            return "TYPE_VIEW_CLICKED";
        case AccessibilityEvent.TYPE_VIEW_FOCUSED:
            return "TYPE_VIEW_FOCUSED";
        case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
            return "TYPE_VIEW_HOVER_ENTER";
        case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
            return "TYPE_VIEW_HOVER_EXIT";
        case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
            return "TYPE_VIEW_LONG_CLICKED";
        case AccessibilityEvent.TYPE_VIEW_SCROLLED:
            return "TYPE_VIEW_SCROLLED";
        case AccessibilityEvent.TYPE_VIEW_SELECTED:
            return "TYPE_VIEW_SELECTED";
        case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
            return "TYPE_VIEW_TEXT_CHANGED";
        case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
            return "TYPE_VIEW_TEXT_SELECTION_CHANGED";
        case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
            return "TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY";
        case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
            return "TYPE_WINDOWS_CHANGED";
        case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
            return "TYPE_WINDOW_CONTENT_CHANGED";
        case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            return "TYPE_WINDOW_STATE_CHANGED";
        }
        return String.format("unknown (%d)", eventType);
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void dumpNode(AccessibilityNodeInfo node, int indent) {
        if (node == null) {
            Log.v(TAG, "node is null (stopping iteration)");
            return;
        }

        String indentStr = new String(new char[indent * 3]).replace('\0', ' ');
        Log.v(TAG, String.format("%s NODE: %s", indentStr, node.toString()));
        for (int i = 0; i < node.getChildCount(); i++) {
            dumpNode(node.getChild(i), indent + 1);
        }
        /* NOTE: Not sure if this is really required. Documentation is unclear. */
        node.recycle();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        /* Show the accessibility event */
        Log.v(TAG, String.format(
                  "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
                  getEventTypeString(event.getEventType()), event.getClassName(), event.getPackageName(),
                  event.getEventTime(), getEventText(event)));

        /* Show all the windows available */
        List<AccessibilityWindowInfo> windows = getWindows();
        Log.v(TAG, String.format("Windows (%d):", windows.size()));
        for (AccessibilityWindowInfo window : windows) {
            Log.v(TAG, String.format("window: %s", window.toString()));
        }

        /* Dump the view hierarchy */
        dumpNode(getRootInActiveWindow(), 0);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.v(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
            AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS |
            AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
            AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }
}
