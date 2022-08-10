package br.com.br.sincronizacaoreceita.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.br.sincronizacaoreceita.model.Receita;
import br.com.br.sincronizacaoreceita.repository.ReceitaRepository;
import br.com.br.sincronizacaoreceita.service.ReceitaService.ReceitaServico;

@Service
public class ReceitaServiceImpl implements ReceitaServico{
	
	  @Autowired
	  ReceitaRepository receitaRepo;

	  public void addReceita(Receita receita) {

		  receitaRepo.save(receita);

	  }

	  public List <Receita> getReceitaOfList() {

	    return receitaRepo.findAll();

	  }

}
