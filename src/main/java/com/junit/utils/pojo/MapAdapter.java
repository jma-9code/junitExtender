package com.junit.utils.pojo;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.sun.xml.txw2.annotation.XmlElement;

class MapElements {
    @XmlAttribute
    public String name;
    @XmlAttribute
    public String value;

    private MapElements() {
    } //Required by JAXB

    public MapElements(String key, String value) {
        this.name = key;
        this.value = value;
    }
}


public class MapAdapter extends XmlAdapter<MapElements[], Map<String, String>> {
    public MapAdapter() {
    }

    public MapElements[] marshal(Map<String, String> arg0) throws Exception {
        MapElements[] mapElements = new MapElements[arg0.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : arg0.entrySet())
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

        return mapElements;
    }

    public Map<String, String> unmarshal(MapElements[] arg0) throws Exception {
        Map<String, String> r = new HashMap<String, String>();
        for (MapElements mapelement : arg0)
            r.put(mapelement.name, mapelement.value);
        return r;
    }
}