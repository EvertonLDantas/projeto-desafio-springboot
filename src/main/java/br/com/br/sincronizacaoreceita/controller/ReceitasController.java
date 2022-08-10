package br.com.br.sincronizacaoreceita.controller;

import br.com.br.sincronizacaoreceita.model.Receita;
import br.com.br.sincronizacaoreceita.repository.ReceitaRepository;
import br.com.br.sincronizacaoreceita.service.ReceitaService;
import br.com.br.sincronizacaoreceita.service.ReceitaService.ReceitaServico;
import br.com.br.sincronizacaoreceita.util.CsvFileGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/receitas/")
public class ReceitasController {

    private final ReceitaRepository receitaRepository;
    
    @Autowired
    private ReceitaServico receitaServico;

    @Autowired
    private CsvFileGenerator csvGenerator;

    @Autowired
    public ReceitasController(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @GetMapping("cadastrar")
    public String showCadastrarForm(Receita receita) {
        return "add-receita";
    }

    @GetMapping("listar")
    public String showUpdateForm(Model model) {
        model.addAttribute("receitas", receitaRepository.findAll());
        return "index";
    }

    @PostMapping("adicionar")
    public String addReceita(@Valid Receita receita, BindingResult result, Model model) throws RuntimeException, InterruptedException {
    	boolean validacao = ReceitaService.atualizarConta(receita.getAgencia(), receita.getConta(),  Long.parseLong(receita.getSaldo()), receita.getStatus());
        if (result.hasErrors()&& validacao) {
            return "add-receita";
        }

        receitaRepository.save(receita);
        return "redirect:listar";
    }

    @GetMapping("editar/{id}")
    public String showEditarForm(@PathVariable("id") long id, Model model) {
    	Receita receita = receitaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid receita Id:" + id));
        model.addAttribute("receita", receita);
        return "update-receita";
    }

    @PostMapping("update/{id}")
    public String atualizarReceita(@PathVariable("id") long id, @Valid Receita receita, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            receita.setId(id);
            return "update-receita";
        }

        receitaRepository.save(receita);
        model.addAttribute("receitas", receitaRepository.findAll());
        return "index";
    }

    @GetMapping("deletar/{id}")
    public String deletarReceita(@PathVariable("id") long id, Model model) {
    	Receita receita = receitaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid receita Id:" + id));
        receitaRepository.delete(receita);
        model.addAttribute("receitas", receitaRepository.findAll());
        return "index";
    }
    
    @GetMapping("/export-to-csv")
    public void exportIntoCSV(HttpServletResponse response) throws IOException {
      response.setContentType("text/csv");
      response.addHeader("Content-Disposition", "attachment; filename=\"receita.csv\"");
      csvGenerator.writeReceitasToCsv(receitaServico.getReceitaOfList(), response.getWriter());
    }
    
    
    @PostMapping("/upload")
    public String uploadData(@RequestParam("file") MultipartFile file) throws Exception{
    	List<Receita> receitas = new ArrayList<>();
    	InputStream inputStream = file.getInputStream();
    	CsvParserSettings setting = new CsvParserSettings();
    	setting.setHeaderExtractionEnabled(true);
    	CsvParser parser = new CsvParser(setting);
    	List<com.univocity.parsers.common.record.Record> parseAllRecords = parser.parseAllRecords(inputStream);
    	parseAllRecords.forEach(record ->{
    		Receita receita = new Receita();
    		receita.setId(Long.parseLong(record.getString("id")));
    		receita.setAgencia("agenda");
    		receita.setConta("conta");
    		receita.setSaldo("saldo");
    		receita.setStatus("status");
    		receitas.add(receita);
    	});
    	receitaRepository.saveAll(receitas);
		return "Importação feita com sucesso.";
    	
    }
}