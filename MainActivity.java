package com.example.autocompletetextviewdb;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

	
	CustomAutoCompleteView myAutoComplete;

	// adapter para o auto-complete
	ArrayAdapter<String> myAdapter;

	DatabaseHandler databaseH;

	// apenas para add um valor inicial
	String[] item = new String[] { "Please search..." };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {

			// instanciando o database handler
			databaseH = new DatabaseHandler(MainActivity.this);

			// inserindo um simples dado para o database
			insertSampleData();

			// autocompletetextview no activity_main.xml
			myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);

			// add o listener que vai sugerir os dados
			myAutoComplete
					.addTextChangedListener(new CustomAutoCompleteTextChangedListener(
							this));

			// setando o adapter
			myAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, item);
			myAutoComplete.setAdapter(myAdapter);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSampleData() {

		// CREATE
		databaseH.create(new MyObject("Janeiro"));
		databaseH.create(new MyObject("Fevereiro"));
		databaseH.create(new MyObject("Mar�o"));
		databaseH.create(new MyObject("Abril"));
		databaseH.create(new MyObject("Maio"));
		databaseH.create(new MyObject("Junho"));
		databaseH.create(new MyObject("Julho"));
		databaseH.create(new MyObject("Agosto"));
		databaseH.create(new MyObject("Setembro"));
		databaseH.create(new MyObject("Outubro"));
		databaseH.create(new MyObject("Novembro"));
		databaseH.create(new MyObject("Dezembro"));


	}

	// esta fun��o � usada no CustomAutoCompleteTextChangedListener.java
	public String[] getItemsFromDb(String searchTerm) {

		// add itens dinamicamente no array
		List<MyObject> products = databaseH.read(searchTerm);
		int rowCount = products.size();

		String[] item = new String[rowCount];
		int x = 0;

		for (MyObject record : products) {

			item[x] = record.objectName;
			x++;
		}

		return item;
	}

}
