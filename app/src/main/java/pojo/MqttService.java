package pojo;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;

/**
 *         (  ・ω・)✄╰ひ╯
 * Created by Joe on 2017/12/24.
 * 這個 Service 可以進行MQTT初始化連線的動作
 * import 時注意別跟org.eclipse.paho.android.service.MqttService搞錯了 !
 */

public class MqttService {

    private MqttConnectOptions options;
    private String clientId;
    private MqttAndroidClient client;
    private IMqttToken token;

    public MqttService(Context context) {
        initOptions();
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, "tcp://" + ServerConnectData.MQTT_IP + ":" + ServerConnectData.MQTT_PORT, clientId);
        try {
            token = getClient().connect(getOptions());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void initOptions() {
        options = new MqttConnectOptions();
        options.setUserName(ServerConnectData.MQTT_USERNAME);
        options.setPassword(ServerConnectData.MQTT_PASSWORD.toCharArray());
    }

    public MqttAndroidClient getClient() {
        return client;
    }

    public MqttConnectOptions getOptions() {
        return options;
    }

    public IMqttToken getToken() {
        return token;
    }
}
