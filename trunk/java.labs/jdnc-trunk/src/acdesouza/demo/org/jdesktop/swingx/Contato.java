package org.jdesktop.swingx;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class Contato {
	private Long id;
	private String nome;
	private ContatoStatus status;
	private Date nascimento;
	private boolean ativo;
    private BigDecimal total;

	public Contato(String nome, Date nascimento){
		this.id		=	null;
		this.nome	=	nome;
		this.nascimento = nascimento;
	}
	public Contato(Object[] dados){
		this.nome	=	dados[0] + "";
		this.status	=	ContatoStatus.valueOf(dados[1]+"");
		this.nascimento	=	(Date)dados[2];
		this.id		=	Long.valueOf( dados[3] + "" );
	}

    public Date getNascimento() {
    	return nascimento;
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }

    public Integer getIdade() {
    	Calendar hoje = Calendar.getInstance();
    	Calendar nascimento = Calendar.getInstance();
    	nascimento.setTime( getNascimento() );

    	return hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);
    }

    public String getNome() {
    	return nome;
    }
	
    public void setNome(String nome) {
    	this.nome = nome;
    }
	
    public ContatoStatus getStatus() {
    	return status;
    }
	
    public void setStatus(ContatoStatus status) {
    	this.status = status;
    }
	public boolean isAtivo() {
    	return ativo;
    }
	public void setAtivo(boolean ativo) {
    	this.ativo = ativo;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}