
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.com.previsao.model.Historico;
import br.com.previsao.model.Linha;
import br.com.previsao.model.Produto;

public class SalvandoProduto2 {
	public static Date addDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 25);
		return cal.getTime();
	}
	

	public static void main(String[] args) throws FileNotFoundException {
		List<Produto> lista = new ArrayList<>();
		
		Calendar jan1998 = Calendar.getInstance();
		jan1998.set(1998, 1, 21);
		jan1998.setTime(jan1998.getTime());
		
		
		Calendar fev1998 = Calendar.getInstance();
		fev1998.set(1998, 2, 21);
		fev1998.setTime(fev1998.getTime());
		
		Calendar mar1998 = Calendar.getInstance();
		mar1998.set(1998, 3, 21);
		mar1998.setTime(mar1998.getTime());
		
		Calendar abr1998 = Calendar.getInstance();
		abr1998.set(1998, 4, 21);
		abr1998.setTime(abr1998.getTime());
		
		Calendar mai1998 = Calendar.getInstance();
		mai1998.set(1998, 5, 21);
		mai1998.setTime(mai1998.getTime());
				
		
		Calendar jun1998 = Calendar.getInstance();
		jun1998.set(1998, 6, 21);
		jun1998.setTime(jun1998.getTime());
		
		
		Calendar jul1998 = Calendar.getInstance();
		jul1998.set(1998, 7, 21);
		jul1998.setTime(jul1998.getTime());
		
		
		Calendar ago1998 = Calendar.getInstance();
		ago1998.set(1998, 8, 21);
		ago1998.setTime(ago1998.getTime());
		
		
		Calendar set1998 = Calendar.getInstance();
		set1998.set(1998, 9, 21);
		set1998.setTime(set1998.getTime());
		
		Calendar out1998 = Calendar.getInstance();
		out1998.set(1998, 10, 21);
		out1998.setTime(out1998.getTime());
		
		Calendar nov1998 = Calendar.getInstance();
		nov1998.set(1998, 11, 21);
		
		Calendar dez1998 = Calendar.getInstance();
		dez1998.set(1998, 12, 21);
		
		Calendar jan1999 = Calendar.getInstance();
		jan1999.set(1999, 01, 21);
		
		Calendar fev1999 = Calendar.getInstance();
		fev1999.set(1999, 02, 21);
		
		Calendar mar1999 = Calendar.getInstance();
		mar1999.set(1999, 03, 21);
		
		Calendar abr1999 = Calendar.getInstance();
		abr1999.set(1999, 04, 21);
		
		Calendar mai1999 = Calendar.getInstance();
		mai1999.set(1999, 05, 21);
		
		Calendar jun1999 = Calendar.getInstance();
		jun1999.set(1999, 06, 21);
		
		Calendar jul1999 = Calendar.getInstance();
		jul1999.set(1999, 07, 21);
		
		Calendar ago1999 = Calendar.getInstance();
		ago1999.set(1999, 8, 21);
		
		Calendar set1999 = Calendar.getInstance();
		set1999.set(1999, 9, 21);
		
		Calendar out1999 = Calendar.getInstance();
		out1999.set(1999, 10, 21);
		// DateFormat df = new DateFormat();
//		DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		System.out.println(formatador.format(dez2009.getTime()));
		
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("lista", List.class);
		xstream.alias("produto", Produto.class);
		xstream.alias("historico", Historico.class);
		

		Produto produto1 = new Produto("0124940","FRASCO MEDICINAL VC", new BigDecimal("2.30"));
		
		Produto produto2 = new Produto("0218308","CONEXAO MEDICINAL RD VC-10", new BigDecimal("15"));
		
		Produto produto3 = new Produto("0907499","ACENDEDOR P/MACARICO", new BigDecimal("18"));
		
		Linha linha1 = new Linha();
		linha1.setDescricao("DRAGEADO");
		produto1.setLinha(linha1);
		
		Historico hist1 = new Historico();
		hist1.setMesesHistoricos(jan1998.getTime());
		hist1.setQuantidade(44);
		hist1.setProduto(produto1);
		
		Historico hist2 = new Historico();
		hist2.setMesesHistoricos(fev1998.getTime());
		hist2.setQuantidade(35);
		hist2.setProduto(produto1);
		
		Historico hist3 = new Historico();
		hist3.setMesesHistoricos(mar1998.getTime());
		hist3.setQuantidade(248);
		hist3.setProduto(produto1);
		
		Historico hist4 = new Historico();
		hist4.setMesesHistoricos(abr1998.getTime());
		hist4.setQuantidade(343);
		hist4.setProduto(produto1)
		;
		Historico hist5 = new Historico();
		hist5.setMesesHistoricos(mai1998.getTime());
		hist5.setQuantidade(166);
		hist5.setProduto(produto1);
		
		Historico hist6 = new Historico();
		hist6.setMesesHistoricos(jun1998.getTime());
		hist6.setQuantidade(45);
		hist6.setProduto(produto1);
		
		Historico hist7 = new Historico();
		hist7.setMesesHistoricos(jul1998.getTime());
		hist7.setQuantidade(333);
		hist7.setProduto(produto1);
		
		Historico hist8 = new Historico();
		hist8.setMesesHistoricos(ago1998.getTime());
		hist8.setQuantidade(165);
		hist8.setProduto(produto1);
		
		Historico hist9 = new Historico();
		hist9.setMesesHistoricos(set1998.getTime());
		hist9.setQuantidade(86);
		hist9.setProduto(produto1);
		
		Historico hist10 = new Historico();
		hist10.setMesesHistoricos(out1998.getTime());
		hist10.setQuantidade(124);
		hist10.setProduto(produto1);
		
		Historico hist11 = new Historico();
		hist11.setMesesHistoricos(nov1998.getTime());
		hist11.setQuantidade(136);
		hist11.setProduto(produto1);
		
		Historico hist12 = new Historico();
		hist12.setMesesHistoricos(dez1998.getTime());
		hist12.setQuantidade(133);
		hist12.setProduto(produto1);
		
		Historico hist13 = new Historico();
		hist13.setMesesHistoricos(jan1999.getTime());
		hist13.setQuantidade(82);
		hist13.setProduto(produto1);
		
		Historico hist14 = new Historico();
		hist14.setMesesHistoricos(fev1999.getTime());
		hist14.setQuantidade(59);
		hist14.setProduto(produto1);
		
		Historico hist15 = new Historico();
		hist15.setMesesHistoricos(mar1999.getTime());
		hist15.setQuantidade(169);
		hist15.setProduto(produto1);
	
		Historico hist16 = new Historico();
		hist16.setMesesHistoricos(abr1999.getTime());
		hist16.setQuantidade(133);
		hist16.setProduto(produto1);
		
		Historico hist17 = new Historico();
		hist17.setMesesHistoricos(mai1999.getTime());
		hist17.setQuantidade(69);
		hist17.setProduto(produto1);
		
		Historico hist18 = new Historico();
		hist18.setMesesHistoricos(jun1999.getTime());
		hist18.setQuantidade(333);
		hist18.setProduto(produto1);
		
		Historico hist19 = new Historico();
		hist19.setMesesHistoricos(jul1999.getTime());
		hist19.setQuantidade(112);
		hist19.setProduto(produto1);
		

//---------- produto 2 --------------//
		Linha linha2 = new Linha();
		linha2.setDescricao("CREME DE AVELA E CACAU");
		produto1.setLinha(linha2);
		
		Historico hist21 = new Historico();
		hist21.setMesesHistoricos(jan1998.getTime());
		hist21.setQuantidade(92);
		hist21.setProduto(produto2);
		
		Historico hist22 = new Historico();
		hist22.setMesesHistoricos(fev1998.getTime());
		hist22.setQuantidade(65);
		hist22.setProduto(produto2);
		
		
		Historico hist23 = new Historico();
		hist23.setMesesHistoricos(mar1998.getTime());
		hist23.setQuantidade(123);
		hist23.setProduto(produto2);
		
		Historico hist24 = new Historico();
		hist24.setMesesHistoricos(abr1998.getTime());
		hist24.setQuantidade(115);
		hist24.setProduto(produto2);
		
		Historico hist25 = new Historico();
		hist25.setMesesHistoricos(mai1998.getTime());
		hist25.setQuantidade(103);
		hist25.setProduto(produto2);
		
		Historico hist26 = new Historico();
		hist26.setMesesHistoricos(jun1998.getTime());
		hist26.setQuantidade(202);
		hist26.setProduto(produto2);
		
		Historico hist27 = new Historico();
		hist27.setMesesHistoricos(jul1998.getTime());
		hist27.setQuantidade(136);
		hist27.setProduto(produto2);
		
		Historico hist28 = new Historico();
		hist28.setMesesHistoricos(ago1998.getTime());
		hist28.setQuantidade(114);
		hist28.setProduto(produto2);
		
		Historico hist29 = new Historico();
		hist29.setMesesHistoricos(set1998.getTime());
		hist29.setQuantidade(147);
		hist29.setProduto(produto2);
		
		Historico hist210 = new Historico();
		hist210.setMesesHistoricos(out1998.getTime());
		hist210.setQuantidade(90);
		hist210.setProduto(produto2);
		
		Historico hist211 = new Historico();
		hist211.setMesesHistoricos(nov1998.getTime());
		hist211.setQuantidade(104);
		hist211.setProduto(produto2);
		
		Historico hist212 = new Historico();
		hist212.setMesesHistoricos(dez1998.getTime());
		hist212.setQuantidade(156);
		hist212.setProduto(produto2);
		
		Historico hist213 = new Historico();
		hist213.setMesesHistoricos(jan1999.getTime());
		hist213.setQuantidade(21);
		hist213.setProduto(produto2);
		
		Historico hist214 = new Historico();
		hist214.setMesesHistoricos(fev1999.getTime());
		hist214.setQuantidade(104);
		hist214.setProduto(produto2);
		
		Historico hist215 = new Historico();
		hist215.setMesesHistoricos(mar1999.getTime());
		hist215.setQuantidade(158);
		hist215.setProduto(produto2);
		
		Historico hist216 = new Historico();
		hist216.setMesesHistoricos(abr1999.getTime());
		hist216.setQuantidade(156);
		hist216.setProduto(produto2);
		
		Historico hist217 = new Historico();
		hist217.setMesesHistoricos(mai1999.getTime());
		hist217.setQuantidade(72);
		hist217.setProduto(produto2);
		
		Historico hist218 = new Historico();
		hist218.setMesesHistoricos(jun1999.getTime());
		hist218.setQuantidade(136);
		hist218.setProduto(produto2);
		
		Historico hist219 = new Historico();
		hist219.setMesesHistoricos(jul1999.getTime());
		hist219.setQuantidade(87);
		hist219.setProduto(produto2);
		
		//-----------produto 3 ------------//
		
		Linha linha3 = new Linha();
		linha3.setDescricao("CHOCOLATE");
		produto1.setLinha(linha3);
		
		Historico hist31 = new Historico();
		hist31.setMesesHistoricos(jan1998.getTime());
		hist31.setQuantidade(224);
		hist31.setProduto(produto3);
		
		Historico hist32 = new Historico();
		hist32.setMesesHistoricos(fev1998.getTime());
		hist32.setQuantidade(96);
		hist32.setProduto(produto3);
		
		Historico hist33 = new Historico();
		hist33.setMesesHistoricos(mar1998.getTime());
		hist33.setQuantidade(436);
		hist33.setProduto(produto3);
		
		Historico hist34 = new Historico();
		hist34.setMesesHistoricos(abr1998.getTime());
		hist34.setQuantidade(315);
		hist34.setProduto(produto3)
		;
		Historico hist35 = new Historico();
		hist35.setMesesHistoricos(mai1998.getTime());
		hist35.setQuantidade(188);
		hist35.setProduto(produto3);
		
		Historico hist36 = new Historico();
		hist36.setMesesHistoricos(jun1998.getTime());
		hist36.setQuantidade(323);
		hist36.setProduto(produto3);
		
		Historico hist37 = new Historico();
		hist37.setMesesHistoricos(jul1998.getTime());
		hist37.setQuantidade(592);
		hist37.setProduto(produto3);
		
		Historico hist38 = new Historico();
		hist38.setMesesHistoricos(ago1998.getTime());
		hist38.setQuantidade(462);
		hist38.setProduto(produto3);
		
		Historico hist39 = new Historico();
		hist39.setMesesHistoricos(set1998.getTime());
		hist39.setQuantidade(116);
		hist39.setProduto(produto3);
		
		Historico hist310 = new Historico();
		hist310.setMesesHistoricos(out1998.getTime());
		hist310.setQuantidade(142);
		hist310.setProduto(produto3);
		
		Historico hist311 = new Historico();
		hist311.setMesesHistoricos(nov1998.getTime());
		hist311.setQuantidade(349);
		hist311.setProduto(produto3);
		
		Historico hist312 = new Historico();
		hist312.setMesesHistoricos(dez1998.getTime());
		hist312.setQuantidade(223);
		hist312.setProduto(produto3);
		
		Historico hist313 = new Historico();
		hist313.setMesesHistoricos(jan1999.getTime());
		hist313.setQuantidade(339);
		hist313.setProduto(produto3);
		
		Historico hist314 = new Historico();
		hist314.setMesesHistoricos(fev1999.getTime());
		hist314.setQuantidade(298);
		hist314.setProduto(produto3);
		
		Historico hist315 = new Historico();
		hist315.setMesesHistoricos(mar1999.getTime());
		hist315.setQuantidade(1088);
		hist315.setProduto(produto3);
		
		Historico hist316 = new Historico();
		hist316.setMesesHistoricos(abr1999.getTime());
		hist316.setQuantidade(392);
		hist316.setProduto(produto3);
		
		Historico hist317 = new Historico();
		hist317.setMesesHistoricos(mai1999.getTime());
		hist317.setQuantidade(472);
		hist317.setProduto(produto3);
		
		Historico hist318 = new Historico();
		hist318.setMesesHistoricos(jun1999.getTime());
		hist318.setQuantidade(592);
		hist318.setProduto(produto3);
		
		Historico hist319 = new Historico();
		hist319.setMesesHistoricos(jul1999.getTime());
		hist319.setQuantidade(428);
		hist319.setProduto(produto3);
		
		produto1.getHistoricos().add(hist1);
		produto1.getHistoricos().add(hist2);
		produto1.getHistoricos().add(hist3);
		produto1.getHistoricos().add(hist4);
		produto1.getHistoricos().add(hist5);
		produto1.getHistoricos().add(hist6);
		produto1.getHistoricos().add(hist7);
		produto1.getHistoricos().add(hist8);
		produto1.getHistoricos().add(hist9);
		produto1.getHistoricos().add(hist10);
		produto1.getHistoricos().add(hist11);
		produto1.getHistoricos().add(hist12);
		produto1.getHistoricos().add(hist13);
		produto1.getHistoricos().add(hist14);
		produto1.getHistoricos().add(hist15);
		produto1.getHistoricos().add(hist16);
		produto1.getHistoricos().add(hist17);
		produto1.getHistoricos().add(hist18);
		produto1.getHistoricos().add(hist19);
				
		produto2.getHistoricos().add(hist21);
		produto2.getHistoricos().add(hist22);
		produto2.getHistoricos().add(hist23);
		produto2.getHistoricos().add(hist24);
		produto2.getHistoricos().add(hist25);
		produto2.getHistoricos().add(hist26);
		produto2.getHistoricos().add(hist27);
		produto2.getHistoricos().add(hist28);
		produto2.getHistoricos().add(hist29);
		produto2.getHistoricos().add(hist210);
		produto2.getHistoricos().add(hist211);
		produto2.getHistoricos().add(hist212);
		produto2.getHistoricos().add(hist213);
		produto2.getHistoricos().add(hist214);
		produto2.getHistoricos().add(hist215);
		produto2.getHistoricos().add(hist216);
		produto2.getHistoricos().add(hist217);
		produto2.getHistoricos().add(hist218);
		produto2.getHistoricos().add(hist219);
		
		produto3.getHistoricos().add(hist31);
		produto3.getHistoricos().add(hist32);
		produto3.getHistoricos().add(hist33);
		produto3.getHistoricos().add(hist34);
		produto3.getHistoricos().add(hist35);
		produto3.getHistoricos().add(hist36);
		produto3.getHistoricos().add(hist37);
		produto3.getHistoricos().add(hist38);
		produto3.getHistoricos().add(hist39);
		produto3.getHistoricos().add(hist310);
		produto3.getHistoricos().add(hist311);
		produto3.getHistoricos().add(hist312);
		produto3.getHistoricos().add(hist313);
		produto3.getHistoricos().add(hist314);
		produto3.getHistoricos().add(hist315);
		produto3.getHistoricos().add(hist316);
		produto3.getHistoricos().add(hist317);
		produto3.getHistoricos().add(hist318);
		produto3.getHistoricos().add(hist319);
		
		lista.add(produto1);
		lista.add(produto2);
		lista.add(produto3);
		
		String xml = xstream.toXML(lista);
		System.out.println(xml);
		OutputStream os = new FileOutputStream("./produto2.xml");
		xstream.toXML(lista, os);
		
//		List<Produto> carrinho = (List<Produto>) xstream.fromXML(xml);
//		
//		for (Produto p : carrinho) {
//			System.out.println(p.getCodigoProduto());
//			System.out.println(p.getDescricao());
//			System.out.println(p.getValor());
//			System.out.println(p.getHistoricos());
//		}

		

	}

}