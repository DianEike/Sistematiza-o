package com.diandev.sistematizacao.service;

import com.diandev.sistematizacao.models.Profissional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

    private List<Profissional> profissionais = new ArrayList<>();
    private final String modo = "json"; // ou "xml"

    @PostConstruct
    public void init() {
        try {
            if (modo.equals("json")) {
                carregarJson();
            } else {
                carregarXml();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream input = getClass().getClassLoader().getResourceAsStream("profissionais.json");
        Map<String, List<Profissional>> map = mapper.readValue(input, new TypeReference<>() {});
        profissionais = map.get("profissionais");
    }

    private void carregarXml() throws Exception {
        InputStream input = getClass().getClassLoader().getResourceAsStream("profissionais.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);

        NodeList nodes = doc.getElementsByTagName("profissional");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element e = (Element) nodes.item(i);
            String nome = e.getElementsByTagName("nome").item(0).getTextContent();
            String especialidade = e.getElementsByTagName("especialidade").item(0).getTextContent();
            profissionais.add(new Profissional(nome, especialidade));
        }
    }

    public List<Profissional> buscar(String nome, String especialidade) {
        return profissionais.stream().filter(p ->
            (nome == null || p.getNome().toLowerCase().contains(nome.toLowerCase())) &&
            (especialidade == null || p.getEspecialidade().equalsIgnoreCase(especialidade))
        ).collect(Collectors.toList());
    }
}