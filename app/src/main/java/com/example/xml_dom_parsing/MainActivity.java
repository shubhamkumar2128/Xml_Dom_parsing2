package com.example.xml_dom_parsing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    PlaceFragment placeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1212);
        }
        if (savedInstanceState == null) {
            placeFragment = new PlaceFragment();
            getSupportFragmentManager().beginTransaction().add(placeFragment, "placefrag").commit();
        } else {
            placeFragment = (PlaceFragment) getSupportFragmentManager().findFragmentByTag("placefrag");
        }
        placeFragment.startTask();
    }


    public static class PlaceFragment extends Fragment {
        Task task;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setRetainInstance(true);
        }

        public void startTask() {
            if (task != null)
                task.cancel(true);
            else {
                task = new Task();
                task.execute();
            }
        }
    }

    public static class Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String downloadUrl = "https://www.w3schools.com/xml/simple.xml";
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                progressXml(inputStream);

            } catch (MalformedURLException e) {
                Log.d("msg", e.getMessage());
            } catch (IOException e) {
                Log.d("msg", e.getMessage());
            } catch (SAXException e) {
                Log.d("msg", e.getMessage());
            } catch (ParserConfigurationException e) {
                Log.d("msg", e.getMessage());
            }
            return null;
        }

        private void progressXml(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("food");
            NodeList childnodelist = null;
            Node node = null;
            Node childnode = null;

            for (int x = 0; x < nodeList.getLength(); x++) {
                node = nodeList.item(x);
                Log.d("msg", node.getNodeName());
                childnodelist = node.getChildNodes();
                for (int j = 0; j < childnodelist.getLength(); j++) {
                    childnode = childnodelist.item(j);
                    Log.d("msg", childnode.getTextContent());
                }


            }
        }

    }

}
