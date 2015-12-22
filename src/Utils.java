import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;
import util.android.crypt.MD5;
import util.android.util.DateUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by jeff on 21/12/2015.
 */
public class Utils implements ToolWindowFactory {
    private ToolWindow myToolWindow;
    private Project project;
    private JPanel rootContent;
    private JLabel timeNow;
    private JLabel dateNow;
    private JTextField md5IN;
    private JTextField md5Out;
    private JButton GOButton;
    private JTextField textField1;
    private JButton GOButton1;
    private JLabel convertLabel;
    private JLabel ip;
    private JLabel externalIP;
    private JButton updateButton;
    private JButton APICallButton;
    private JLabel tSeconds;
    private JLabel tdf2;
    private JTextField textField2;
    private JButton GOButton2;
    private JTextField textField3;

    java.util.Timer timer;

    public Utils() {
        GOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                md5Out.setText(MD5.md5(md5IN.getText()));
            }
        });
        GOButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField1.getText() == null || textField1.getText().equalsIgnoreCase("")) {
                    convertLabel.setText(null);
                    return;
                }
                try {
                    long l = Long.parseLong(textField1.getText());
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(l));
                    convertLabel.setText(DateUtils.formatAtomDate(c.getTime()));
                } catch (Exception err) {
                    Date d = DateUtils.parseAtomDate(textField1.getText(), TimeZone.getDefault());
                    convertLabel.setText("" + d.getTime());
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshIP();
            }
        });

        externalIP.addComponentListener(new ComponentAdapter() {
        });
        APICallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        APICallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Map<String, String> items = new HashMap<>();
                items.put("Java", "public void getExternalIP() {\n    OkHttpClient client = new OkHttpClient();\n" +
                        "    Request.Builder builder = new Request.Builder();\n" +
                        "    builder.url(\"https://api.ipify.org/\").get();\n    client.newCall(builder.build()).enqueue(new Callback() {\n" +
                        "            @Override\n" +
                        "            public void onFailure(Request request, IOException e) {\n" +
                        "\n" +
                        "            }\n" +
                        "\n" +
                        "            @Override\n" +
                        "            public void onResponse(Response response) throws IOException {\n" +
                        "                \n" +
                        "            }\n" +
                        "        });\n}");
                items.put("PHP", "<?php\n" +
                        "    $ip = file_get_contents('https://api.ipify.org');\n" +
                        "    echo \"My public IP address is: \" . $ip;\n" +
                        "?>");
                items.put("Javascript", "<script type=\"application/javascript\">\n" +
                        "  function getIP(json) {\n" +
                        "    document.write(\"My public IP address is: \", json.ip);\n" +
                        "  }\n" +
                        "</script>\n" +
                        "\n" +
                        "<script type=\"application/javascript\" src=\"https://api.ipify.org?format=jsonp&callback=getIP\"></script>");
                items.put("jQuery", "<script type=\"application/javascript\">\n" +
                        "  $(function() {\n" +
                        "    $.getJSON(\"https://api.ipify.org?format=jsonp&callback=?\",\n" +
                        "      function(json) {\n" +
                        "        document.write(\"My public IP address is: \", json.ip);\n" +
                        "      }\n" +
                        "    );\n" +
                        "  });\n" +
                        "</script>");
                items.put("Bash", "#!/bin/bash\n" +
                        "\n" +
                        "ip=$(curl -s https://api.ipify.org)\n" +
                        "echo \"My public IP address is: $ip\"");

                APICall dialog = new APICall(items);
                dialog.setLocationByPlatform(true);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        GOButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField2.getText() == null || textField2.getText().equalsIgnoreCase("")) {
                    textField3.setText(null);
                    return;
                }

                byte[] b = textField2.getText().getBytes(StandardCharsets.UTF_8);
                textField3.setText(bytesToHex(b));
            }
        });
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        StringBuilder b = new StringBuilder();
        b.append("{");
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            b.append("0x");
            b.append(hexArray[v >>> 4]);
            b.append(hexArray[v & 0x0F]);
            b.append(", ");
        }
        b.deleteCharAt(b.lastIndexOf(","));
        b.deleteCharAt(b.lastIndexOf(" "));
        b.append("}");
        return b.toString();
    }

    private void refreshIP() {
        try {
            ip.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (Exception e) {}

        externalIP.setText("-");

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url("https://api.ipify.org/").get();

        client.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                externalIP.setText(response.body().string());
            }
        });
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        myToolWindow = toolWindow;
        this.project = project;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(rootContent, "", false);
        toolWindow.getContentManager().addContent(content);

        timer = new java.util.Timer();
        timer.schedule(new DateTask(), 0, 1000);

        refreshIP();
    }

    class DateTask extends TimerTask {

        @Override
        public void run() {
            Date d = new Date();
            timeNow.setText("" + (d.getTime()));
            tSeconds.setText("" + d.getTime()/1000);
            dateNow.setText(DateUtils.formatAtomDate(d));
            tdf2.setText(DateUtils.formatLongDate(d));
        }
    }
}
