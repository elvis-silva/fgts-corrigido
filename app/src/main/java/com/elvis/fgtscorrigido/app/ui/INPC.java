package com.elvis.fgtscorrigido.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Iterator;

import com.elvis.fgtscorrigido.app.MainActivity;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.utils.Indice;

public class INPC extends AbstractFragment {

    private GridView gridView;

    private ArrayList<String> dados;
    private ArrayList<Indice> indices;
    private ArrayList<Indice> indices480;
    private ArrayAdapter<String> adapter;

    static final String[] months = {
            "Ano", "Janeiro", "Fevereiro", "Março", "Abril",
            "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro",
            "Novembro", "Dezembro"
    };

    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_inpc, container, false);

        context = contentView.getContext();

        dados = new ArrayList<>();
        createGrid(contentView);
        return contentView;
    }
/*
    public INPC () {
        dados = new ArrayList<>();
        indices = FGTSCorrigido.indices;
        indices480 = FGTSCorrigido.indices480;
    }
*/
    public void mountGridXLarge() {
        int i = 0;
        while (i < 13) {
            dados.add(months[i]);
            i++;
        }
        printIndice(indices);
    }

    public void createGrid(View _view) {
        gridView = (GridView) _view.findViewById(R.id.gridView1);

        adapter = new ArrayAdapter<>(context, R.layout.text_size_15, dados);
        gridView.setAdapter(adapter);

        mountGridScreenCel();
    }

    public void mountGridScreenCel() {
        int i = 0;
        int j = 0;
        Iterator iterator = MainActivity.getInstance().indices.iterator();
        while (iterator.hasNext()) {
            Indice curIndice = (Indice) iterator.next();
            if (curIndice.type.equals("inpc")) {
                if (j == 4) {
                    j = 0;
                    i++;
                }
                if (i == 0 && j == 0) {
                    dados.add("");
                    dados.add("Ano");
                    dados.add("1999");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 1 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 2 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 3 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2000");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 4 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 5 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 6 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2001");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 7 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 8 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 9 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2002");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 10 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 11 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 12 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2003");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 13 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 14 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 15 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2004");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 16 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 17 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 18 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2005");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 19 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 20 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 21 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2006");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 22 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 23 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 24 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2007");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 25 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 26 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 27 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2008");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 28 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 29 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 30 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2009");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 31 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 32 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 33 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2010");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 34 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 35 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 36 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2011");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 37 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 38 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 39 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2012");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 40 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 41 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 42 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2013");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 43 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 44 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 45 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2014");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 46 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 47 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                } else if (i == 48 && j == 0) {
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("");
                    dados.add("Ano");
                    dados.add("2015");
                    dados.add("");
                    dados.add("Janeiro");
                    dados.add("Fevereiro");
                    dados.add("Março");
                    dados.add("Abril");
                } else if (i == 49 && j == 0) {
                    dados.add("Maio");
                    dados.add("Junho");
                    dados.add("Julho");
                    dados.add("Agosto");
                } else if (i == 50 && j == 0) {
                    dados.add("Setembro");
                    dados.add("Outubro");
                    dados.add("Novembro");
                    dados.add("Dezembro");
                }
                dados.add(curIndice.value);
                j++;
            }
        }
    }

    public void mountGridScreen480(ArrayList<Indice> _indices) {
        int i = 0;
        int j = 0;
        Iterator it = _indices.iterator();
        while(it.hasNext()) {
            Indice currentIndice = (Indice) it.next();
            if (currentIndice.type.equals("inpc")) {
                if (j == 4) {
                    j = 0;
                    i++;
                }
                if (i == 0 && j == 0) {
                    dados.add("Ano");
                    dados.add("1999");
                    dados.add("2000");
                    dados.add("2001");
                    dados.add("2002");
                    dados.add("Janeiro");
                } else if (i == 1 && j == 0) {
                    dados.add("Fevereiro");
                } else if (i == 2 && j == 0) {
                    dados.add("Março");
                } else if (i == 3 && j == 0) {
                    dados.add("Abril");
                } else if (i == 4 && j == 0) {
                    dados.add("Maio");
                } else if (i == 5 && j == 0) {
                    dados.add("Junho");
                } else if (i == 6 && j == 0) {
                    dados.add("Julho");
                } else if (i == 7 && j == 0) {
                    dados.add("Agosto");
                } else if (i == 8 && j == 0) {
                    dados.add("Setembro");
                } else if (i == 9 && j == 0) {
                    dados.add("Outubro");
                } else if (i == 10 && j == 0) {
                    dados.add("Novembro");
                } else if (i == 11 && j == 0) {
                    dados.add("Dezembro");
                } else if (i == 12 && j == 0) {
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("Ano");
                    dados.add("2003");
                    dados.add("2004");
                    dados.add("2005");
                    dados.add("2006");
                    dados.add("Janeiro");
                } else if (i == 13 && j == 0) {
                    dados.add("Fevereiro");
                } else if (i == 14 && j == 0) {
                    dados.add("Março");
                } else if (i == 15 && j == 0) {
                    dados.add("Abril");
                } else if (i == 16 && j == 0) {
                    dados.add("Maio");
                } else if (i == 17 && j == 0) {
                    dados.add("Junho");
                } else if (i == 18 && j == 0) {
                    dados.add("Julho");
                } else if (i == 19 && j == 0) {
                    dados.add("Agosto");
                } else if (i == 20 && j == 0) {
                    dados.add("Setembro");
                } else if (i == 21 && j == 0) {
                    dados.add("Outubro");
                } else if (i == 22 && j == 0) {
                    dados.add("Novembro");
                } else if (i == 23 && j == 0) {
                    dados.add("Dezembro");
                }else if (i == 24 && j == 0) {
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("Ano");
                    dados.add("2007");
                    dados.add("2008");
                    dados.add("2009");
                    dados.add("2010");
                    dados.add("Janeiro");
                } else if (i == 25 && j == 0) {
                    dados.add("Fevereiro");
                } else if (i == 26 && j == 0) {
                    dados.add("Março");
                } else if (i == 27 && j == 0) {
                    dados.add("Abril");
                } else if (i == 28 && j == 0) {
                    dados.add("Maio");
                } else if (i == 29 && j == 0) {
                    dados.add("Junho");
                } else if (i == 30 && j == 0) {
                    dados.add("Julho");
                } else if (i == 31 && j == 0) {
                    dados.add("Agosto");
                } else if (i == 32 && j == 0) {
                    dados.add("Setembro");
                } else if (i == 33 && j == 0) {
                    dados.add("Outubro");
                } else if (i == 34 && j == 0) {
                    dados.add("Novembro");
                } else if (i == 35 && j == 0) {
                    dados.add("Dezembro");
                }else if (i == 36 && j == 0) {
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("----------");
                    dados.add("Ano");
                    dados.add("2011");
                    dados.add("2012");
                    dados.add("2013");
                    dados.add("2014");
                    dados.add("Janeiro");
                } else if (i == 37 && j == 0) {
                    dados.add("Fevereiro");
                } else if (i == 38 && j == 0) {
                    dados.add("Março");
                } else if (i == 39 && j == 0) {
                    dados.add("Abril");
                } else if (i == 40 && j == 0) {
                    dados.add("Maio");
                } else if (i == 41 && j == 0) {
                    dados.add("Junho");
                } else if (i == 42 && j == 0) {
                    dados.add("Julho");
                } else if (i == 43 && j == 0) {
                    dados.add("Agosto");
                } else if (i == 44 && j == 0) {
                    dados.add("Setembro");
                } else if (i == 45 && j == 0) {
                    dados.add("Outubro");
                } else if (i == 46 && j == 0) {
                    dados.add("Novembro");
                } else if (i == 47 && j == 0) {
                    dados.add("Dezembro");
                }
                dados.add(currentIndice.value);
                j++;
            }
        }
    }

    private void printIndice (ArrayList<Indice> indices) {

        int i = 1;
        int j = 0;
        Iterator it = indices.iterator();
        while(it.hasNext()) {
            Indice currentIndice = (Indice) it.next();
            if (currentIndice.type.equals("inpc")) {
                if (j == 12) {
                    j = 0;
                    i++;
                }
                if (i == 1 && j == 0) {
                    dados.add("1999");
                } else if (i == 2 && j == 0) {
                    dados.add("2000");
                } else if (i == 3 && j == 0) {
                    dados.add("2001");
                } else if (i == 4 && j == 0) {
                    dados.add("2002");
                } else if (i == 5 && j == 0) {
                    dados.add("2003");
                } else if (i == 6 && j == 0) {
                    dados.add("2004");
                } else if (i == 7 && j == 0) {
                    dados.add("2005");
                } else if (i == 8 && j == 0) {
                    dados.add("2006");
                } else if (i == 9 && j == 0) {
                    dados.add("2007");
                } else if (i == 10 && j == 0) {
                    dados.add("2008");
                } else if (i == 11 && j == 0) {
                    dados.add("2009");
                } else if (i == 12 && j == 0) {
                    dados.add("2010");
                } else if (i == 13 && j == 0) {
                    dados.add("2011");
                } else if (i == 14 && j == 0) {
                    dados.add("2012");
                } else if (i == 15 && j == 0) {
                    dados.add("2013");
                } else if (i == 16 && j == 0) {
                    dados.add("2014");
                } else if(i == 17 && j == 0) {
                    dados.add("2015");
                }
                dados.add(currentIndice.value);
                j++;
            }
        }
    }
}
