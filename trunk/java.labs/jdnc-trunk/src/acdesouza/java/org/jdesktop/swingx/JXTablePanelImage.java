package org.jdesktop.swingx;

import javax.swing.ImageIcon;

//TODO Split in one to each plugin an another to the JXTablePlugin
public enum JXTablePanelImage {
	CHART("document_chart")
	, CHART_LINE("chart-line")
    , EXPORTAR_EXCEL("excel")
	, FILTER_ADD("filtro_add")
	, FILTER_DELETE("filtro_del")
	, DELETE("document_delete")
	;

	private String caminhoImagem;
	private String extensaoDaImagem=".png";
	private JXTablePanelImage(String caminhoImagem){
		this.caminhoImagem = "/imagens/24x24/" + caminhoImagem;
//		this.caminhoImagem = "/imagens" + caminhoImagem;
	}
	private JXTablePanelImage(String caminhoImagem, String extension){
		this(caminhoImagem);
		extensaoDaImagem = extension;
	}
	
	public ImageIcon getImage(){
		return new ImageIcon(getClass().getResource(caminhoImagem + extensaoDaImagem));
	}
}
