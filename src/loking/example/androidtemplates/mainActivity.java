package loking.example.androidtemplates;

import java.net.MalformedURLException;
import java.net.URL;

import loking.example.Collector.CollectorActivity;
import loking.example.Collector.CollectorClient;
import android.os.Bundle;

public class mainActivity extends CollectorActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		URL url = null;
		try {
			url = new URL("http://zh.wikipedia.org/w/api.php");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CollectorClient cc= new CollectorClient(url);
		cc.sendRequestWithHttpGETURLConnection();
		cc.sendRequestWithHttpPOSTURLConnection("action=opensearch&search=Android");
	}

}
