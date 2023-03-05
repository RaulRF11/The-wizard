/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.wizard.superwizard;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author raulr 
 */
public class escribirXML {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document documento = null;
    Element raizResultados;
    Source source;
    Result resultado;
    public escribirXML() {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation dom = builder.getDOMImplementation();
            documento = dom.createDocument(null,"archivo", null);

            raizResultados = documento.createElement("Resultados");
            documento.getDocumentElement().appendChild(raizResultados);
            
            source = new DOMSource(documento);
            resultado = new StreamResult(new File("C:\\Users\\raulr\\Documents\\NetBeansProjects\\the-wizard\\core\\src\\main\\java\\net\\wizard\\superwizard\\archivo.xml"));
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
    
    public void escribirResultado(ArrayList<Resultado> resultados) throws TransformerException {
        for(int i = 0; i < resultados.size(); i++) {
            Element nodoResultados, nodoDatos;
            Text texto;

            nodoResultados = documento.createElement("Resultado");
            raizResultados.appendChild(nodoResultados);

            nodoDatos = documento.createElement("id");
            nodoResultados.appendChild(nodoDatos);

            //String id = String.valueOf(resultados[i].getId());
            //texto = documento.createTextNode(id);
            //nodoDatos.appendChild(texto);

            nodoDatos = documento.createElement("resultado");
            nodoResultados.appendChild(nodoDatos);

            String resultado1 = String.valueOf(resultados.get(i).getResultado());
            texto = documento.createTextNode(resultado1);
            nodoDatos.appendChild(texto);

            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source, resultado);
            } catch (TransformerConfigurationException tce) {
                tce.printStackTrace();
            } catch (TransformerException te) {
                te.printStackTrace();
            }
        }
    }
}
