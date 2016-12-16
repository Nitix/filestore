package org.filestore.ejb.config;

import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by nitix on 14/12/16.
 */
public class OAuthConfig {

    private HashMap<OAuthProviderType, ProviderConfiguration> configurations = new HashMap<>();

    @PostConstruct
    public void configure() {
        try {
            InputStream fXmlFile = this.getClass().getResourceAsStream("/oauth2-configuration.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("configuration");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    OAuthProviderType provider = OAuthProviderType.valueOf(eElement.getElementsByTagName("provider").item(0).getTextContent().toUpperCase());
                    this.configurations.put(provider, new ProviderConfiguration(
                            provider,
                            eElement.getElementsByTagName("clientId").item(0).getTextContent(),
                            eElement.getElementsByTagName("clientSecrect").item(0).getTextContent(),
                            eElement.getElementsByTagName("redirectUri").item(0).getTextContent()
                    ));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<OAuthProviderType, ProviderConfiguration> getConfigurations() {
        return configurations;
    }

    public ProviderConfiguration getProviderConfiguration(OAuthProviderType providerType) {
        return configurations.get(providerType);
    }
}
