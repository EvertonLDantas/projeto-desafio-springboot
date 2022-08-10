package br.com.br.sincronizacaoreceita.util;

import java.io.IOException;

import java.io.Writer;

import java.util.List;
import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVPrinter;

import org.springframework.stereotype.Component;

import br.com.br.sincronizacaoreceita.model.Receita;



@Component
public class CsvFileGenerator {

public void writeReceitasToCsv(List<Receita> receitas, Writer writer) {

try {

CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

for (Receita receita : receitas) {

printer.printRecord(receita.getId(), receita.getAgencia(), receita.getConta(),

receita.getSaldo(), receita.getStatus());

}

} catch (IOException e) {

e.printStackTrace();
}

}

}