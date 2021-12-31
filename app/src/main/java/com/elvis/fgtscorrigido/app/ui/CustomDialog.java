package com.elvis.fgtscorrigido.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.elvis.fgtscorrigido.app.AppManager;
import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.MainActivity;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.context.Contexts;
import com.elvis.fgtscorrigido.app.data.DataHandler;
import com.elvis.fgtscorrigido.app.utils.Calcule;
import com.elvis.fgtscorrigido.app.utils.GUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CustomDialog extends Dialog implements View.OnClickListener {

    public static String TAG = "CustomDialog";

    public static final String TYPE_MONTHS = "mounths";
    public static final String TYPE_YEARS = "years";
    public static final String TYPE_ABOUT = "about";
    public static final String TYPE_TIPS = "tips";
    public static final String TYPE_CLEAR = "clear";
    public static final String TYPE_SAVED = "saved";
    public static final String TYPE_SAVE = "save";

    public TYPE CURRENT_TYPE;
    private String monthInitial, monthFinal, yearInitial, yearFinal;
    private TextView promptView;
    public AdapterRowView adapterRowView;
    public ArrayAdapterView arrayAdapterView;

    public enum TYPE {
        ESCOLHER_PERIODO,
        CHANGE_VALUE,
        SET_NAME,
        ABOUT,
        LOAD_CALCULE
    }

    private String[] chooseText = { "Escolha o primeiro mês", "Escolha o primeiro ano",
            "Escolha o último mês", "Escolha o último ano"
    };

    private Spinner monthsInitials, monthsFinals, yearsInitials, yearsFinals;
    SimpleAdapter simpleAdapter;
    List<Map<String, Object>> monthsMapList;
//    private static String[] from = {AppManager.DISPLAY, AppManager.DISPLAY};
//    private static int[] to = {R.id.tex, R.id.simple_spdditem_display};

    private String dialogType;
    public int step = 0;
    public String currentActivity = "";
    private String nameSet = "";
    private EditText nameSetEditText;
    public String nameSelected = "";

    public View lastViewSelected;
    private Context context;
    private LinearLayout container;
    private ProgressBar progressBar;
    private ArrayList<String> names;

    public CustomDialog(Context pContext, CharSequence title, CharSequence message, String type) {
        super(pContext);

        context = pContext;

        dialogType = type.equals("") ? "" : type;

        buildDialog(title, message, dialogType);

        buildView();
    }

    public CustomDialog(TYPE pType) {
        super(Contexts.getInstance().getCurrentActivity(), R.style.DialogBackgroundStyle);

        context = Contexts.getInstance().getContext();

        buildType(pType);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MainActivity.getInstance().fabMain.setClickable(true);
                MainActivity.getInstance().fabLoad.setClickable(true);
                MainActivity.getInstance().fabAddNew.setClickable(true);
            }
        });
    }

    private void buildType(TYPE pType) {
        CURRENT_TYPE = pType;
        switch (CURRENT_TYPE) {
            case ESCOLHER_PERIODO:
                buildDialogEscolherPeriodo();
                break;
            case CHANGE_VALUE:
                buildDialogChangeValue();
                break;
            case SET_NAME:
                buildDialogSetName();
                break;
            case LOAD_CALCULE:
                buildDialogLoadCalcule();
                break;
            case ABOUT:
                buildDialogAbout();
                break;
        }
    }

    private void buildDialogAbout() {
        setContentView(R.layout.dialog_about);

        String message = "\nAplicativo desenvolvido apenas para SIMULAÇÃO DA CORREÇÃO DO FGTS pelos índices TR e INPC a partir do ano de 1999 com a finalidade de " +
                "informar ao trabalhador brasileiro sobre as diferenças aplicadas entre esses índices na atualização do FGTS.\n";
        message += "\nEnquanto aguardamos a decisão referente Ação Direta de Inconstitucionalidade (ADI 5090) no Supremo Tribunal Federal (STF) " +
                "contra dispositivos das Leis 8.036/1990 (artigo 13) e 8.177/1991 (artigo 17) que impõem a correção dos depósitos nas " +
                "contas vinculadas do FGTS pela TR você pode entrar com um processo contra a Caixa " +
                "Econômica Federal pedindo a revisão e correção do seu FGTS aplicando o INPC sobre o saldo no período, " +
                "isso é um direito de todo cidadão brasileiro, afinal o Brasil é um país democrático que permite cada um de nós cobrar os direitos " +
                "conquistados ao longo de nossa história.\n";
        message += "\nESTEJA CIENTE QUE OS VALORES DOS RESULTADOS MOSTRADOS NESSSE APLICATIVO SÃO VALORES APROXIMADOS E POR ISSO PODEM NÃO ESTAR EXATAMENTE IGUAIS AO EXTRATO DO FGTS.\n";
        message += "\nÉ IMPRESCINDÍVEL CONSULTAR UM ADVOGADO OU O SINDICATO PARA MAIS DETALHES.\n";
        message += "\nEsse aplicativo não tem nenhum vínculo com partido político ou órgão governamental ou não governamental, " +
                "assim a finalidade é meramente informativa assegurada pela Constituição Federal Brasileira artigo 5º inciso IX onde se diz que " +
                "é livre a expressão da atividade intelectual, artística, científica e de comunicação, independentemente de censura ou licença.\n";
        message += "\n\n Versão do aplicativo 3.1.0\n";
        message += "\nNow It Goes Apps\n";
        message += "\ne-mail: now.it.goes.apps@gmail.com\n";
//        message += "\nCopyright © 2014 - 2015\n";
        message += "\n\nObrigado por adquirir o aplicativo.\n\n";

        TextView text = (TextView) findViewById(R.id.aboutText);
        text.setText(message);

        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void buildDialogLoadCalcule() {
        setContentView(R.layout.dialog_load_calcule);

        names = new ArrayList<>();

        LinearLayout btnContainer = (LinearLayout) findViewById(R.id.namesContainer);
        DataHandler dataHandler = Contexts.getInstance().getDataHandler();
        Cursor cursor = dataHandler.returnNamesData();
        cursor.moveToFirst();
        int i = 0;
        while (i < cursor.getCount()) {
            names.add(dataHandler.getNameData(cursor));
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        if(names.size() > 0) {
            Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
            i = 0;
            while (i < names.size()) {
                Button btn = new Button(context);
                btnContainer.addView(btn);
                btn.setText(names.get(i));
                btn.setTextSize(25f);
                btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));
                btn.setBackgroundResource(R.drawable.btn);
                btn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if(lastViewSelected != null) lastViewSelected.setBackgroundResource(R.drawable.btn);
                            lastViewSelected = view;
                            view.setBackgroundColor(Color.argb(255, 200, 200, 255));
                            nameSelected = ((Button) view).getText().toString();
                            MainActivity.getInstance().getFragment().nameSave = nameSelected;
                        }
                        return false;
                    }
                });
                i++;
            }
        }

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.NAME_SET = "";
                nameSelected = "";
                dismiss();
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameSelected.equals("")) {
                    setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            AbstractFragment fragment = MainActivity.getInstance().getFragment();
                            AppManager.NAME_SET = nameSelected;
                            fragment.setState(FGTSPeriodos.STATE.DELETE_DATA);
                        }
                    });
                    dismiss();
                } else {
                    GUI.toastLong("Escolha um nome primeiro!");
                }
            }
        });

        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameSelected.equals("")) {
                    setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            AbstractFragment fragment = MainActivity.getInstance().getFragment();
                            AppManager.NAME_SET = nameSelected;
                            fragment.setState(FGTSPeriodos.STATE.LOADING_DATA);
                        }
                    });
                    dismiss();
                } else {
                    GUI.toastLong("Escolha um nome primeiro!");
                }
            }
        });
    }

    private void buildDialogSetName() {
        setContentView(R.layout.dialog_set_name);
        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().getFragment().setState(FGTSPeriodos.STATE.DATA_READY);
                dismiss();
            }
        });

        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = ((EditText) findViewById(R.id.nameSet)).getText().toString().trim();
                if(Contexts.getInstance().getDataHandler().containsName(newName)) {
                    GUI.toastLong("Nome já existe! Escolha outro.");
                    return;
                }
                if(!((EditText) findViewById(R.id.nameSet)).getText().toString().trim().equals("")) {
                    setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            AbstractFragment fragment = MainActivity.getInstance().getFragment();
                            AppManager.NAME_SET = newName;
                            fragment.setState(FGTSPeriodos.STATE.SAVING_PROGRESS);
                        }
                    });
                    dismiss();
                } else {
                    GUI.toastLong("Digite um nome");
                }
            }
        });
    }

    private void buildDialogChangeValue() {
        setContentView(R.layout.dialog_change_value);
        promptView = (TextView) findViewById(R.id.promptValue);

        findViewById(R.id.btnNum0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(0));
            }
        });

        findViewById(R.id.btnNum1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(1));
            }
        });

        findViewById(R.id.btnNum2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(2));
            }
        });

        findViewById(R.id.btnNum3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(3));
            }
        });

        findViewById(R.id.btnNum4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(4));
            }
        });

        findViewById(R.id.btnNum5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(5));
            }
        });

        findViewById(R.id.btnNum6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(6));
            }
        });

        findViewById(R.id.btnNum7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(7));
            }
        });

        findViewById(R.id.btnNum8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(8));
            }
        });

        findViewById(R.id.btnNum9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptView.setText(addToPrompt(9));
            }
        });

        findViewById(R.id.btnClean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCharToPrompt();
            }
        });

        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDotToPrompt();
            }
        });


        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) lastViewSelected).setText(getValueInPrompt());
                adapterRowView.setDeposito(getValueInPrompt());
                arrayAdapterView.notifyDataSetChanged();
                dismiss();
            }
        });
    }

    private String getValueInPrompt() {
        String valueInPrompt = promptView.getText().toString();
        if(!valueInPrompt.contains(",")) {
            valueInPrompt += ",00";
            return valueInPrompt;
        }
        if((valueInPrompt.indexOf(",") + 2) == valueInPrompt.length()) {
            valueInPrompt += "0";
            return valueInPrompt;
        }
        if(String.valueOf(valueInPrompt.charAt(valueInPrompt.length() - 1)).equals(",")) {
            valueInPrompt += "00";
        }
        return valueInPrompt;
    }

    private void addDotToPrompt() {
        String newPromptValue = promptView.getText().toString();
        if(!newPromptValue.contains(",")) {
            newPromptValue += ",";
            promptView.setText(newPromptValue);
        }
    }

    private void removeCharToPrompt() {
        String newPromptValue = promptView.getText().length() > 0 ?
                promptView.getText().subSequence(0, promptView.getText().length() - 1).toString() : "0";
        if(promptView.getText().toString().length() == 1) newPromptValue = "0";
        showInPrompt(newPromptValue);
    }

    private String addToPrompt(String pData) {
        String newPromptValue = promptView.getText().toString();
        if(newPromptValue.contains(",")) {
            if((newPromptValue.indexOf(",") + 3) == newPromptValue.length()) {
                return newPromptValue;
            }
        }
        newPromptValue = promptView.getText().toString().equals("0") ? pData :
                (promptView.getText().toString() + pData);
        return newPromptValue;
    }

    private String addToPrompt(int pData) {
        String newPromptValue = promptView.getText().toString();
        if(newPromptValue.contains(",")) {
            if((newPromptValue.indexOf(",") + 3) == newPromptValue.length()) {
                return newPromptValue;
            }
        }
        newPromptValue = newPromptValue.equals("0") ? String.valueOf(pData) :
                (newPromptValue + String.valueOf(pData));

        return newPromptValue;
    }

    public void showInPrompt(String pData) {
        promptView.setText(pData);
    }

    public void changeTitle(String pNewTitle) {
        ((TextView) findViewById(R.id.dialogTitle)).setText(pNewTitle);
    }

    private void buildDialogEscolherPeriodo() {
        setContentView(R.layout.dialog_escolher_periodo);

        ArrayAdapter<String> dataMonthsAdapter = new ArrayAdapter<>(context,
                R.layout.simple_spinner_item, AppManager.MONTHS);
        dataMonthsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> dataYearsAdapter = new ArrayAdapter<>(context,
                R.layout.simple_spinner_item, AppManager.YEARS);
        dataYearsAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        monthsInitials = (Spinner) findViewById(R.id.monthsInitials);
        monthsInitials.setAdapter(dataMonthsAdapter);
        monthsInitials.setSelection(0);
        monthsInitials.setOnItemSelectedListener(new MyOnItemSelectedListener());
        monthInitial = AppManager.MONTHS[0];

        yearsInitials = (Spinner) findViewById(R.id.yearsInitials);
        yearsInitials.setAdapter(dataYearsAdapter);
        yearsInitials.setSelection(0);
        yearsInitials.setOnItemSelectedListener(new MyOnItemSelectedListener());
        yearInitial = AppManager.YEARS[0];

        monthsFinals = (Spinner) findViewById(R.id.monthsFinals);
        monthsFinals.setAdapter(dataMonthsAdapter);
        monthsFinals.setSelection(0);
        monthsFinals.setOnItemSelectedListener(new MyOnItemSelectedListener());
        monthFinal = AppManager.MONTHS[0];

        yearsFinals = (Spinner) findViewById(R.id.yearsFinals);
        yearsFinals.setAdapter(dataYearsAdapter);
        yearsFinals.setSelection(0);
        yearsFinals.setOnItemSelectedListener(new MyOnItemSelectedListener());
        yearFinal = AppManager.YEARS[0];

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().fabMain.setClickable(true);
                dismiss();
            }
        });

        findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Calcule.periodsOK(monthInitial, yearInitial, monthFinal, yearFinal)) {
                    /*if(monthFinal.toLowerCase().equals("dezembro") && yearFinal.toLowerCase().equals("2015") ||
                            monthInitial.toLowerCase().equals("dezembro") && yearInitial.toLowerCase().equals("2015")) {
                        GUI.toastLong("Dezembro de 2015 ainda sem cálculo.");
                    } else {*/
                        setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                MainActivity.getInstance().getFragment().handleBtns();
                            }
                        });
                        createData();

                        dismiss();
                   // }
                } else {
                    GUI.toastLong("Período incorreto!");
                }
            }
        });
    }

    private void createData() {
        MainActivity.getInstance().getFragment().createPeriodo(
                monthInitial, yearInitial, monthFinal, yearFinal
        );
    }

    private void buildDialog(CharSequence title, CharSequence message, String dialogType) {
        if (dialogType.equals(TYPE_MONTHS)) {
            buildDialogChooseMonth();
        } else if (dialogType.equals(TYPE_YEARS)) {
            buildDialogChooseYears();
        } else if (dialogType.equals(TYPE_ABOUT)) {
            buildDialogShowAbout(title);
        } else if (dialogType.equals(TYPE_TIPS)) {
            buildDialogShowTips(title);
        } else if (dialogType.equals(TYPE_CLEAR)) {
            buildDialogClear(title);
        } else if (dialogType.equals(TYPE_SAVED)) {
            buildDialogSaved(title);
        } else if (dialogType.equals(TYPE_SAVE)) {
            buildDialogSave(title);
        } else {
            setContentView(R.layout.custom_dialog);
            setTitle(title);

            TextView text = (TextView) findViewById(R.id.text);
            text.setText("\n" + message + "\n");

            Button dialogButton = (Button) findViewById(R.id.dialogButtonOK);

            TableRow tableRow = (TableRow) findViewById(R.id.btnsRow);
            tableRow.removeView(findViewById(R.id.dialogButtonDelete));
            tableRow.removeView(findViewById(R.id.dialogButtonReturn));

            dialogButton.setOnClickListener(this);
        }
        this.dialogType = dialogType;
    }

    private void buildDialogSave(CharSequence pTitle) {
        setContentView(R.layout.custom_dialog_save);
        setTitle(pTitle);

        nameSetEditText = (EditText) findViewById(R.id.nameSet);

        Button confirmBtn = (Button) findViewById(R.id.confirmBtn);
        confirmBtn.setText(R.string.salvar);
        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);

        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void buildDialogSaved(CharSequence pTitle) {
        setContentView(R.layout.custom_dialog);
        setTitle(pTitle);
        names = new ArrayList<>();

        LinearLayout btnContainer = (LinearLayout) findViewById(R.id.container);
        TextView textView = (TextView) findViewById(R.id.text);
        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        Cursor cursor = dataHandler.returnNamesData();
        cursor.moveToFirst();
        int i = 0;
        while (i < cursor.getCount()) {
            names.add(dataHandler.getNameData(cursor));
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        if(names.size() > 0) {
            Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
            i = 0;
            while (i < names.size()) {
                Button btn = new Button(context);
                btnContainer.addView(btn);
                btn.setText(names.get(i));
                btn.setTextSize(25f);
                btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                btn.setBackgroundResource(R.drawable.btn);
                btn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if(lastViewSelected != null) lastViewSelected.setBackgroundResource(R.drawable.btn);
                            lastViewSelected = view;
                            view.setBackgroundColor(Color.argb(255, 200, 200, 255));
                            nameSelected = ((Button) view).getText().toString();
                        }
                        return false;
                    }
                });
                i++;
            }
        } else {
            textView.setText("\n\nVocê não tem nenhum cálculo gravado.\n\n");
            FGTSCorrigido.getInstance().getFgtsPeriodos().showBtnInitScreen();
        }

        Button okButton = (Button) findViewById(R.id.dialogButtonOK);
        okButton.setText(R.string.carregar);
        Button deleteBtn = (Button) findViewById(R.id.dialogButtonDelete);
        Button returnBtn = (Button) findViewById(R.id.dialogButtonReturn);

        okButton.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
    }

    private void buildDialogClear(CharSequence pTitle) {
        setContentView(R.layout.custom_dialog_clear);
        setTitle(pTitle);

        String message = "\nTem certeza que deseja limpar o cálculo?\n";

        TextView text = (TextView) findViewById(R.id.text);
        text.setText(message);

        Button dialogButtonOK = (Button) findViewById(R.id.dialogButtonOK);
        Button dialogButtonNO = (Button) findViewById(R.id.dialogButtonNO);

        dialogButtonOK.setOnClickListener(this);
        dialogButtonNO.setOnClickListener(this);
    }

    private void buildDialogShowAbout(CharSequence pTitle) {
        setContentView(R.layout.custom_dialog_about);
        String message = "\nAplicativo desenvolvido apenas para SIMULAÇÃO DA CORREÇÃO DO FGTS pelos índices TR e INPC desde o ano de 1999 com a finalidade de " +
                "informar ao trabalhador brasileiro sobre as diferenças aplicadas desses índices na atualização do FGTS.\n";
        message += "\nEnquanto aguardamos a decisão referente Ação Direta de Inconstitucionalidade (ADI 5090) no Supremo Tribunal Federal (STF) " +
                "contra dispositivos das Leis 8.036/1990 (artigo 13) e 8.177/1991 (artigo 17) que impõem a correção dos depósitos nas " +
                "contas vinculadas do FGTS pela TR você pode entrar com um processo contra a Caixa " +
                "Econômica Federal pedindo a revisão e correção do seu FGTS aplicando o INPC sobre o saldo no período, " +
                "isso é um direito de todo cidadão brasileiro, afinal o Brasil é um país democrático que permite cada um de nós cobrar os direitos " +
                "conquistados ao longo de nossa história.\n";
        message += "\nESTEJA CIENTE QUE OS VALORES DOS RESULTADOS MOSTRADOS NESSSE APLICATIVO SÃO VALORES APROXIMADOS E POR ISSO PODEM NÃO ESTAR EXATAMENTE IGUAIS AO EXTRATO DO FGTS.\n";
        message += "\nÉ IMPRESCINDÍVEL CONSULTAR UM ADVOGADO OU O SINDICATO PARA MAIS DETALHES.\n";
        message += "\nEsse aplicativo não tem nenhum vínculo com partido político ou órgão governamental ou não governamental, " +
                "assim a finalidade é meramente informativa assegurada pela Constituição Federal Brasileira artigo 5º inciso IX onde se diz que " +
                "é livre a expressão da atividade intelectual, artística, científica e de comunicação, independentemente de censura ou licença.\n";
        message += "\n\n Versão do aplicativo 3.3.0\n";
        message += "\nCriado por Elvis Carlos\n";
//        message += "\ne-mail: elviscarlossouza@gmail.com\n";
//        message += "\nCopyright © 2014 - 2015\n";
        message += "\n\nObrigado por adquirir o aplicativo.\n\n";


        setTitle(pTitle);
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(message);

        Button dialogButton = (Button) findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(this);
    }

    private void buildDialogShowTips(CharSequence pTitle) {
        setContentView(R.layout.custom_dialog_help);
        String message = "- Cálculo -\n";
        message += "\nTecle em [Iniciar] para começar o cálculo e siga as etapas conforme solicitado na tela.\n";
        message += "\nApós escolher o período insira os valores depositados de cada mês conforme seu EXTRATO DO PIS.\n";
        message += "\nCom os valores inseridos tecle em [Calcular] para ver todos os detalhes do cálculo.\n";
        message += "\nVocê pode salvar cada cálculo com um nome específico sempre que desejar teclando em [Salvar].\n";
        message += "\nSe desejar limpe o cálculo atual precionando a tecla [Limpar].\n\n";
        message += "\n- Tabela TR -\n";
        message += "\nTabela para simples conferência dos índices mensais da TR.\n\n";
        message += "\n- Tabela INPC -\n";
        message += "\nTabela para simples conferência dos índices mensais do INPC.\n\n";
        message += "\n- OBSERVAÇÕES -\n";
        message += "\nAPLICATIVO DESENVOLVIDO APENAS PARA SIMPLES CONFERÊNCIA E AUXÍLIO AO USUÁRIO FINAL EM " +
                "RELAÇÃO AO SEU FGTS Á PARTIR DO ANO DE 1999.\n";
        message += "\nOS VALORES DOS RESULTADOS MOSTRADOS SÃO VALORES APROXIMADOS.\n";
        message += "\nEnquanto não sai a decisão do STF você tem que entrar com um processo contra a Caixa " +
                "Econômica Federal pedindo a revisão e correção do seu FGTS durante o período com saldo.\n";
        message += "\nVOCÊ AINDA PODE PEDIR MAIS 1% DE MORA POR MÊS ENTRANDO COM UM PROCESSO JUDICIAL PARA " +
                "REAVER ESSAS DIFERENÇAS.\n";
        message += "\nFAVOR CONSULTAR UM ADVOGADO PARA MAIS DETALHES.\n";
        message += "\n\n\nObrigado por adquirir o aplicativo.\n\n";

        setTitle(pTitle);
        TextView text = (TextView) findViewById(R.id.text);
        text.setGravity(Gravity.LEFT);
        text.setText(message);

        Button dialogButton = (Button) findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(this);
    }

    private void buildDialogChooseMonth() {
        setContentView(R.layout.custom_dialog_months);
        if (step == 0) setTitle(chooseText[step]);
    }

    private void buildDialogChooseYears() {
        setContentView(R.layout.custom_dialog_years);
    }

    @Override
    public void onClick(View v) {
        if (dialogType.equals(TYPE_TIPS) || dialogType.equals(TYPE_ABOUT)) {
            dismiss();
            return;
        }
        if (dialogType.equals(TYPE_SAVE)) {
            switch (v.getId()) {
                case R.id.confirmBtn:
                    dismiss();
                    FGTSCorrigido.fgtsPeriodos.saveData(getNameSet());
                    FGTSCorrigido.getInstance().getFgtsPeriodos().showBtns(null, null, null, 3);
                    break;
                case R.id.cancelBtn:
                    dismiss();
                    break;
            }
            return;
        }
        if(dialogType.equals(TYPE_SAVED)) {
            switch (v.getId()) {
                case R.id.dialogButtonOK:
                    if(lastViewSelected != null) {
                        dismiss();
                        FGTSCorrigido.fgtsPeriodos.loadData(nameSelected);
                        FGTSCorrigido.getInstance().getFgtsPeriodos().showBtns(null, null, null, 3);
                    } else {
                        FGTSCorrigido.fgtsPeriodos.showMessage("Nenhum nome selecionado!");
                    }
                    break;
                case R.id.dialogButtonDelete:
                    if(lastViewSelected != null) {
                        dismiss();
                        if (names.size() == 1) FGTSCorrigido.getInstance().getFgtsPeriodos().showBtns(null, null, 2, null);
                        FGTSCorrigido.fgtsPeriodos.deleteData(nameSelected);
                        ((LinearLayout) lastViewSelected.getParent()).removeView(lastViewSelected);
                        lastViewSelected = null;
                    } else {
                        FGTSCorrigido.fgtsPeriodos.showMessage("Nenhum nome selecionado!");
                    }
                    break;
                case R.id.dialogButtonReturn:
                    dismiss();
                    break;
            }
            return;
        }
        if (this.dialogType.equals(TYPE_CLEAR)) {
            switch (v.getId()) {
                case R.id.dialogButtonOK:
                    if (currentActivity.equals(FGTSCorrigido.getInstance().getFgtsPeriodos().currentyActivity)) {
                        FGTSCorrigido.getInstance().getFgtsPeriodos().clear();
                        FGTSCorrigido.getInstance().getFgtsPeriodos().showBtnInitScreen();
                    }
                    break;
                case R.id.dialogButtonNO:
                    break;
            }
            dismiss();
            return;
        }
        String btnText = String.valueOf(((Button) v).getText());
        if (step <= 3 && !dialogType.equals("")) {

            if (step == 0) {

                FGTSCorrigido.getInstance().getFgtsPeriodos().firstMonth = btnText;

            } else if (step == 1) {

                if (btnText.equals("2015")) {
                    String firstMonthString = FGTSCorrigido.getInstance().getFgtsPeriodos().firstMonth;
                    if (    firstMonthString.equals("Novembro")  ||  firstMonthString.equals("Dezembro"))  {

                        CustomDialog dialog = new CustomDialog(v.getContext(), "A V I S O",
                                "Escolha meses antes de Novembro de 2014.", "");
                        dialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                FGTSCorrigido.getInstance().getFgtsPeriodos().createPeriod();
                            }
                        });
                        dialog.show();

                        dismiss();
                        return;
                    }
                }

                FGTSCorrigido.getInstance().getFgtsPeriodos().firstYear = btnText;

            } else if (step == 2) {

                FGTSCorrigido.getInstance().getFgtsPeriodos().lastMonth = btnText;

            } else {

                String firstMonth = FGTSCorrigido.getInstance().getFgtsPeriodos().firstMonth;
                String firstYear = FGTSCorrigido.getInstance().getFgtsPeriodos().firstYear;
                String lastMonth = FGTSCorrigido.getInstance().getFgtsPeriodos().lastMonth;
                if (!Calcule.periodsOK(firstMonth, firstYear, lastMonth, btnText)) {
                    CustomDialog dialog = new CustomDialog(v.getContext(), "A V I S O",
                            "O último mês tem que ser igual ou posterior ao primeiro mês.", "");
                    dialog.show();
                    dialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            FGTSCorrigido.getInstance().getFgtsPeriodos().createPeriod();
                        }
                    });

                    dismiss();
                    return;
                }

                float lastYearFloat = Float.valueOf(btnText);
                float firstYearFloat = Float.valueOf(FGTSCorrigido.getInstance().getFgtsPeriodos().firstYear);
                if (lastYearFloat < firstYearFloat) {
                    CustomDialog dialog = new CustomDialog(v.getContext(), "A V I S O",
                            "O último ano tem que ser maior ou igual ao primeiro ano.", "");
                    dialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            FGTSCorrigido.getInstance().getFgtsPeriodos().createPeriod();
                        }
                    });
                    dialog.show();

                    dismiss();
                    return;
                }

                if (btnText.equals("2015")) {

                    String lastMonthString = FGTSCorrigido.getInstance().getFgtsPeriodos().lastMonth;
                    if (lastMonthString.equals("Novembro")  ||  lastMonthString.equals("Dezembro"))    {

                        CustomDialog dialog = new CustomDialog(v.getContext(), "A V I S O",
                                "Escolha meses antes de Novembro de 2014.", "");
                        dialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                FGTSCorrigido.getInstance().getFgtsPeriodos().createPeriod();
                            }
                        });
                        dialog.show();

                        dismiss();
                        return;
                    }
                }

                FGTSCorrigido.getInstance().getFgtsPeriodos().lastYear = btnText;

                FGTSCorrigido.getInstance().getFgtsPeriodos().periodAdded = false;

                dismiss();

                FGTSCorrigido.getInstance().getFgtsPeriodos().executeProgress();
                FGTSCorrigido.getInstance().getFgtsPeriodos().showBtns(null, "calc", null, 3);
                return;
            }

            CustomDialog customDialog = dialogType.equals(TYPE_MONTHS) ?
                    new CustomDialog(v.getContext(), "", "", TYPE_YEARS) :
                    new CustomDialog(v.getContext(), "", "", TYPE_MONTHS);
            customDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    FGTSCorrigido.getInstance().getFgtsPeriodos().periodAdded = false;
                }
            });
            customDialog.step = step + 1;
            customDialog.setTitle(chooseText[customDialog.step]);
            customDialog.show();
        }

        dismiss();
    }

    private void buildView() {
        Button btnJaneiro, btnFevereiro, btnMarco, btnAbril, btnMaio, btnJunho, btnJulho, btnAgosto,
                btnSetembro, btnOutubro, btnNovembro, btnDezembro, btn1999, btn2000, btn2001, btn2002,
                btn2003, btn2004, btn2005, btn2006, btn2007, btn2008, btn2009, btn2010, btn2011,
                btn2012, btn2013, btn2014;

        if (dialogType.equals(TYPE_MONTHS)) {

            btnJaneiro = (Button) findViewById(R.id.btnJaneiro);
            btnFevereiro = (Button) findViewById(R.id.btnFevereiro);
            btnMarco = (Button) findViewById(R.id.btnMarco);
            btnAbril = (Button) findViewById(R.id.btnAbril);
            btnMaio = (Button) findViewById(R.id.btnMaio);
            btnJunho = (Button) findViewById(R.id.btnJunho);
            btnJulho = (Button) findViewById(R.id.btnJulho);
            btnAgosto = (Button) findViewById(R.id.btnAgosto);
            btnSetembro = (Button) findViewById(R.id.btnSetembro);
            btnOutubro = (Button) findViewById(R.id.btnOutubro);
            btnNovembro = (Button) findViewById(R.id.btnNovembro);
            btnDezembro = (Button) findViewById(R.id.btnDezembro);

            Button[] btns = {   btnJaneiro, btnFevereiro, btnMarco, btnAbril, btnMaio, btnJunho,
                    btnJulho, btnAgosto, btnSetembro, btnOutubro, btnNovembro, btnDezembro
            };

            int i = 0;
            while (i < btns.length) {
                btns[i].setOnClickListener(this);
                i++;
            }
        } else if (dialogType.equals(TYPE_YEARS)) {
            btn1999 = (Button) findViewById(R.id.btn1999);
            btn2000 = (Button) findViewById(R.id.btn2000);
            btn2001 = (Button) findViewById(R.id.btn2001);
            btn2002 = (Button) findViewById(R.id.btn2002);
            btn2003 = (Button) findViewById(R.id.btn2003);
            btn2004 = (Button) findViewById(R.id.btn2004);
            btn2005 = (Button) findViewById(R.id.btn2005);
            btn2006 = (Button) findViewById(R.id.btn2006);
            btn2007 = (Button) findViewById(R.id.btn2007);
            btn2008 = (Button) findViewById(R.id.btn2008);
            btn2009 = (Button) findViewById(R.id.btn2009);
            btn2010 = (Button) findViewById(R.id.btn2010);
            btn2011 = (Button) findViewById(R.id.btn2011);
            btn2012 = (Button) findViewById(R.id.btn2012);
            btn2013 = (Button) findViewById(R.id.btn2013);
            btn2014 = (Button) findViewById(R.id.btn2014);

            Button[] btns = {   btn1999, btn2000, btn2001, btn2002, btn2003, btn2004, btn2005, btn2006,
                    btn2007, btn2008, btn2009, btn2010, btn2011, btn2012, btn2013, btn2014
            };

            int i = 0;
            while (i < btns.length) {
                btns[i].setOnClickListener(this);
                i++;
            }
        }
    }

    public String getNameSet(){
        return nameSetEditText.getText().toString();
    }

    private class DataViewBinder implements SimpleAdapter.ViewBinder {

        public SelectedName getSelectedName() {
            return null;
        }

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            NamedItem item = (NamedItem) data;
            String name = item.getName();

            if(!(view instanceof TextView)) return false;

            TextView textView = (TextView) view;

            textView.setText("Example");

            return true;
        }
    }

    private class MyOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String itemSelected = parent.getItemAtPosition(position).toString();
            switch (parent.getId()) {
                case R.id.monthsInitials:
                    monthInitial = itemSelected;
                    monthsInitials.setSelection(position);
                    break;
                case R.id.monthsFinals:
                    monthFinal = itemSelected;
                    monthsFinals.setSelection(position);
                    break;
                case R.id.yearsInitials:
                    yearInitial = itemSelected;
                    yearsInitials.setSelection(position);
                    break;
                case R.id.yearsFinals:
                    yearFinal = itemSelected;
                    yearsFinals.setSelection(position);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
