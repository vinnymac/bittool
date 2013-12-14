package com.vinnymac.bittool;

import android.os.AsyncTask;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.service.polling.PollingMarketDataService;

public class Xchange extends AsyncTask<String, Void, Ticker> {

	private Exception exception;

	DataDownloadListener dataDownloadListener;

	public Xchange() {
		// Constructor may be parametric
	}

	public void setDataDownloadListener(
			DataDownloadListener dataDownloadListener) {
		this.dataDownloadListener = dataDownloadListener;
	}

	@Override
	protected Ticker doInBackground(String... params) {
		String exc = params[0];
		String cur = params[1];

		// com.xeiam.xchange.btce.BTCEExchange
		try {
			Class<?> cls = Class.forName(exc);
			Exchange exchange = ExchangeFactory.INSTANCE
					.createExchange(cls.getName());
			PollingMarketDataService marketDataService = exchange
					.getPollingMarketDataService();
			Ticker ticker = marketDataService.getTicker(Currencies.BTC,
					cur);
			return ticker;

		} catch (Exception e) {
			this.exception = e;
			return null;
		}

	}

	protected void onPostExecute(Ticker ticker) {

		if (ticker != null) {
			dataDownloadListener.dataDownloadedSuccessfully(ticker);
		} else{
			dataDownloadListener.dataDownloadFailed();}
		// System.out.println(ticker.toString());
	}

	public static interface DataDownloadListener {
		void dataDownloadedSuccessfully(Ticker data);

		void dataDownloadFailed();
	}

}
