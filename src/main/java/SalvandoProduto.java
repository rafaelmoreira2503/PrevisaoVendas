
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
import br.com.previsao.model.Produto;

public class SalvandoProduto {
	public static Date addDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 25);
		return cal.getTime();
	}
	

	public static void main(String[] args) throws FileNotFoundException {
		List<Produto> lista = new ArrayList<>();
		
		Calendar abr2009 = Calendar.getInstance();
		abr2009.set(2009, 4, 21);
		abr2009.setTime(abr2009.getTime());
		
		
		Calendar mai2009 = Calendar.getInstance();
		mai2009.set(2009, 5, 21);
		mai2009.setTime(mai2009.getTime());
		
		Calendar jun2009 = Calendar.getInstance();
		jun2009.set(2009, 6, 21);
		jun2009.setTime(jun2009.getTime());
		
		Calendar jul2009 = Calendar.getInstance();
		jul2009.set(2009, 7, 21);
		jul2009.setTime(jul2009.getTime());
		
		Calendar ago2009 = Calendar.getInstance();
		ago2009.set(2009, 8, 21);
		ago2009.setTime(ago2009.getTime());
				
		
		Calendar set2009 = Calendar.getInstance();
		set2009.set(2009, 9, 21);
		set2009.setTime(set2009.getTime());
		
		
		Calendar out2009 = Calendar.getInstance();
		out2009.set(2009, 10, 21);
		out2009.setTime(out2009.getTime());
		
		
		Calendar nov2009 = Calendar.getInstance();
		nov2009.set(2009, 11, 21);
		nov2009.setTime(nov2009.getTime());
		
		
		Calendar dez2009 = Calendar.getInstance();
		dez2009.set(2009, 12, 21);
		dez2009.setTime(dez2009.getTime());
		
		Calendar jan2010 = Calendar.getInstance();
		jan2010.set(2010, 01, 21);
		jan2010.setTime(jan2010.getTime());
		
		Calendar fev2010 = Calendar.getInstance();
		fev2010.set(2010, 02, 21);
		
		Calendar mar2010 = Calendar.getInstance();
		mar2010.set(2010, 03, 21);
		
		Calendar abr2010 = Calendar.getInstance();
		abr2010.set(2010, 04, 21);
		
		Calendar mai2010 = Calendar.getInstance();
		mai2010.set(2010, 05, 21);
		
		Calendar jun2010 = Calendar.getInstance();
		jun2010.set(2010, 06, 21);
		// DateFormat df = new DateFormat();
//		DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		System.out.println(formatador.format(dez2009.getTime()));
		
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("lista", List.class);
		xstream.alias("produto", Produto.class);
		xstream.alias("historico", Historico.class);
		

		Produto produto1 = new Produto("1004","PAVESINO 800X15", new BigDecimal("2.30"));
		
		Produto produto2 = new Produto("1261","MABEL ROSCA BAN/CAN 400X28", new BigDecimal("15"));
		
		Produto produto3 = new Produto("1352","ELBIS ROSCA 400X28", new BigDecimal("18"));
		
		Historico hist1 = new Historico();
		hist1.setMesesHistoricos(abr2009.getTime());
		hist1.setQuantidade(36);
		hist1.setProduto(produto1);
		
		Historico hist2 = new Historico();
		hist2.setMesesHistoricos(mai2009.getTime());
		hist2.setQuantidade(48);
		hist2.setProduto(produto1);
		
		Historico hist3 = new Historico();
		hist3.setMesesHistoricos(jun2009.getTime());
		hist3.setQuantidade(26);
		hist3.setProduto(produto1);
		
		Historico hist4 = new Historico();
		hist4.setMesesHistoricos(jul2009.getTime());
		hist4.setQuantidade(31);
		hist4.setProduto(produto1)
		;
		Historico hist5 = new Historico();
		hist5.setMesesHistoricos(ago2009.getTime());
		hist5.setQuantidade(57);
		hist5.setProduto(produto1);
		
		Historico hist6 = new Historico();
		hist6.setMesesHistoricos(set2009.getTime());
		hist6.setQuantidade(42);
		hist6.setProduto(produto1);
		
		Historico hist7 = new Historico();
		hist7.setMesesHistoricos(out2009.getTime());
		hist7.setQuantidade(26);
		hist7.setProduto(produto1);
		
		Historico hist8 = new Historico();
		hist8.setMesesHistoricos(nov2009.getTime());
		hist8.setQuantidade(23);
		hist8.setProduto(produto1);
		
		Historico hist9 = new Historico();
		hist9.setMesesHistoricos(dez2009.getTime());
		hist9.setQuantidade(18);
		hist9.setProduto(produto1);
		
		Historico hist10 = new Historico();
		hist10.setMesesHistoricos(jan2010.getTime());
		hist10.setQuantidade(25);
		hist10.setProduto(produto1);
		
		Historico hist11 = new Historico();
		hist11.setMesesHistoricos(fev2010.getTime());
		hist11.setQuantidade(18);
		hist11.setProduto(produto1);
		
		Historico hist12 = new Historico();
		hist12.setMesesHistoricos(mar2010.getTime());
		hist12.setQuantidade(36);
		hist12.setProduto(produto1);
		
		Historico hist13 = new Historico();
		hist13.setMesesHistoricos(abr2010.getTime());
		hist13.setQuantidade(41);
		hist13.setProduto(produto1);
		
		Historico hist14 = new Historico();
		hist14.setMesesHistoricos(mai2010.getTime());
		hist14.setQuantidade(18);
		hist14.setProduto(produto1);
		
		Historico hist15 = new Historico();
		hist15.setMesesHistoricos(jun2010.getTime());
		hist15.setQuantidade(22);
		hist15.setProduto(produto1);
	
		Historico hist21 = new Historico();
		hist21.setMesesHistoricos(abr2009.getTime());
		hist21.setQuantidade(260);
		hist21.setProduto(produto2);
		
		Historico hist22 = new Historico();
		hist22.setMesesHistoricos(mai2009.getTime());
		hist22.setQuantidade(350);
		hist22.setProduto(produto2);
		
		Historico hist23 = new Historico();
		hist23.setMesesHistoricos(jun2009.getTime());
		hist23.setQuantidade(430);
		hist23.setProduto(produto2);
		
		Historico hist24 = new Historico();
		hist24.setMesesHistoricos(jul2009.getTime());
		hist24.setQuantidade(280);
		hist24.setProduto(produto2);
		
		Historico hist25 = new Historico();
		hist25.setMesesHistoricos(ago2009.getTime());
		hist25.setQuantidade(640);
		hist25.setProduto(produto2);
		
		Historico hist26 = new Historico();
		hist26.setMesesHistoricos(set2009.getTime());
		hist26.setQuantidade(650);
		hist26.setProduto(produto2);
		
		Historico hist27 = new Historico();
		hist27.setMesesHistoricos(out2009.getTime());
		hist27.setQuantidade(240);
		hist27.setProduto(produto2);
		
		Historico hist28 = new Historico();
		hist28.setMesesHistoricos(nov2009.getTime());
		hist28.setQuantidade(430);
		hist28.setProduto(produto2);
		
		Historico hist29 = new Historico();
		hist29.setMesesHistoricos(dez2009.getTime());
		hist29.setQuantidade(320);
		hist29.setProduto(produto2);
		
		Historico hist210 = new Historico();
		hist210.setMesesHistoricos(jan2010.getTime());
		hist210.setQuantidade(585);
		hist210.setProduto(produto2);
		
		Historico hist211 = new Historico();
		hist211.setMesesHistoricos(fev2010.getTime());
		hist211.setQuantidade(385);
		hist211.setProduto(produto2);
		
		Historico hist212 = new Historico();
		hist212.setMesesHistoricos(mar2010.getTime());
		hist212.setQuantidade(450);
		hist212.setProduto(produto2);
		
		Historico hist213 = new Historico();
		hist213.setMesesHistoricos(abr2010.getTime());
		hist213.setQuantidade(760);
		hist213.setProduto(produto2);
		
		Historico hist214 = new Historico();
		hist214.setMesesHistoricos(mai2010.getTime());
		hist214.setQuantidade(690);
		hist214.setProduto(produto2);
		
		Historico hist215 = new Historico();
		hist215.setMesesHistoricos(jun2010.getTime());
		hist215.setQuantidade(400);
		hist215.setProduto(produto2);
		
		Historico hist31 = new Historico();
		hist31.setMesesHistoricos(abr2009.getTime());
		hist31.setQuantidade(128);
		hist31.setProduto(produto3);
		
		Historico hist32 = new Historico();
		hist32.setMesesHistoricos(mai2009.getTime());
		hist32.setQuantidade(180);
		hist32.setProduto(produto3);
		
		Historico hist33 = new Historico();
		hist33.setMesesHistoricos(jun2009.getTime());
		hist33.setQuantidade(96);
		hist33.setProduto(produto3);
		
		Historico hist34 = new Historico();
		hist34.setMesesHistoricos(jul2009.getTime());
		hist34.setQuantidade(108);
		hist34.setProduto(produto3)
		;
		Historico hist35 = new Historico();
		hist35.setMesesHistoricos(ago2009.getTime());
		hist35.setQuantidade(130);
		hist35.setProduto(produto3);
		
		Historico hist36 = new Historico();
		hist36.setMesesHistoricos(set2009.getTime());
		hist36.setQuantidade(150);
		hist36.setProduto(produto3);
		
		Historico hist37 = new Historico();
		hist37.setMesesHistoricos(out2009.getTime());
		hist37.setQuantidade(165);
		hist37.setProduto(produto3);
		
		Historico hist38 = new Historico();
		hist38.setMesesHistoricos(nov2009.getTime());
		hist38.setQuantidade(139);
		hist38.setProduto(produto3);
		
		Historico hist39 = new Historico();
		hist39.setMesesHistoricos(dez2009.getTime());
		hist39.setQuantidade(140);
		hist39.setProduto(produto3);
		
		Historico hist310 = new Historico();
		hist310.setMesesHistoricos(jan2010.getTime());
		hist310.setQuantidade(165);
		hist310.setProduto(produto3);
		
		Historico hist311 = new Historico();
		hist311.setMesesHistoricos(fev2010.getTime());
		hist311.setQuantidade(290);
		hist311.setProduto(produto3);
		
		Historico hist312 = new Historico();
		hist312.setMesesHistoricos(mar2010.getTime());
		hist312.setQuantidade(190);
		hist312.setProduto(produto3);
		
		Historico hist313 = new Historico();
		hist313.setMesesHistoricos(abr2010.getTime());
		hist313.setQuantidade(210);
		hist313.setProduto(produto3);
		
		Historico hist314 = new Historico();
		hist314.setMesesHistoricos(mai2010.getTime());
		hist314.setQuantidade(240);
		hist314.setProduto(produto3);
		
		Historico hist315 = new Historico();
		hist315.setMesesHistoricos(jun2010.getTime());
		hist315.setQuantidade(235);
		hist315.setProduto(produto3);
		
		
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