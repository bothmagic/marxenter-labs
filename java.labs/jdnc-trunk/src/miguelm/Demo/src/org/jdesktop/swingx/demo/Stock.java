package org.jdesktop.swingx.demo;

import java.util.*;
import java.text.CollationKey;
import java.text.Collator;
import java.math.BigDecimal;

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Nov 21, 2007
 * <br>Time: 12:35:11 AM
 */
@SuppressWarnings({"HardCodedStringLiteral", "ClassNameDiffersFromFileName"})
public class Stock {
	/* Since the Ticker Column is editable, we need a way to map Actual stocks
	 * to the ticker symbol typed in by the user. 
	 */
	private static final Map<String, Stock> sStockMap = new WeakHashMap<String, Stock>();
	private static final Map<String, String> sNameLookup = new WeakHashMap<String, String>();
	private static final Map<String, CollationKey> sCollationLookup = new WeakHashMap<String, CollationKey>();
	public static final Collator COLLATOR = Collator.getInstance();

	static {
		storeName("ACME", "Acme Explosives");
		storeName("WEED", "Pippin's Magic Herbs");
		storeName("WAND", "Ollivander's Wand Emporium");
		storeName("SPCE", "arrakis Imports");
		storeName("ROXY", "Hart Media, Inc.");
	}

	private String mTicker;
	private BigDecimal mPrice;

	public Stock(String pTicker, double pPrice) {
		mTicker = pTicker;
		mPrice = BigDecimal.valueOf(pPrice);
		sStockMap.put(pTicker, Stock.this);
	}

	public String getTicker() { return mTicker; }

	public void setTicker(String pTicker) { mTicker = pTicker; }

	public BigDecimal getPrice() { return mPrice; }

	public void setPrice(BigDecimal pPrice) { mPrice = pPrice; }

	public String getName() { return sNameLookup.get(mTicker); }

	public void setName(String pName) {
		storeName(mTicker, pName);
	}

	private static void storeName(String pTicker, String pName) {
		sNameLookup.put(pTicker, pName);
		CollationKey collationKey = COLLATOR.getCollationKey(pName);
		sCollationLookup.put(pTicker, collationKey);
	}

	public static CollationKey getKey(String pTicker) { return sCollationLookup.get(pTicker); }

	public static Stock findStock(String pTicker) {
		if (sStockMap.containsKey(pTicker))
			return sStockMap.get(pTicker);
		Stock stock = new Stock(pTicker, 0.0);
		sStockMap.put(pTicker, stock);
		return stock;
	}
}
