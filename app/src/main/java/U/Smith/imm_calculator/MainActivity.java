package U.Smith.imm_calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView result_text;
    private EditText height, weight, tmgp, kdr, tzs;
    private Switch gender_switch;
    private Double imm_compared, dbl_weight, dbl_height, dbl_tmgp, dbl_kdr, dbl_tzs,
            ppt, ots, mmlg, imm;
    private String compare_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result_text = findViewById(R.id.result_text_View);
        gender_switch = findViewById(R.id.gender_switch);
        height = findViewById(R.id.height_editText);
        weight = findViewById(R.id.weight_editText);
        tmgp = findViewById(R.id.tmgp_editText);
        kdr = findViewById(R.id.kdr_editText);
        tzs = findViewById(R.id.tzs_editText);
        imm_compared = 115.0;

        // код переключателя пола
        gender_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    imm_compared = 95.0;
                    gender_switch.setText("женский");
                }
                else {
                    imm_compared = 115.0;
                    gender_switch.setText("мужской");
                }
            }
        });
    }

    // метод вывода результата при нажатии на кнопку Рассчитать
    public void onClickResult(View view) {
        // проверка заполнения строк
        if ((height.getText().length() == 0) || (weight.getText().length() == 0) ||
                (tmgp.getText().length() == 0) || (kdr.getText().length() == 0)
                || (tzs.getText().length() == 0)) {
            result_text.setText("Ввведены не все данные");
        }
        else {
            // преобразование введенных чисел в тип Double
            dbl_height = Double.parseDouble(height.getText().toString());
            dbl_weight = Double.parseDouble(weight.getText().toString());
            dbl_tmgp = Double.parseDouble(tmgp.getText().toString());
            dbl_kdr = Double.parseDouble(kdr.getText().toString());
            dbl_tzs = Double.parseDouble(tzs.getText().toString());
            // расчет значений
            ppt = calculate_ppt(dbl_height, dbl_weight);
            ots = calculate_ots(dbl_tzs, dbl_kdr);
            mmlg = calculate_mmlg(dbl_tmgp, dbl_kdr, dbl_tzs);
            imm = calculate_imm(mmlg, ppt);
            compare_text = set_result_text(imm_compared, ots, imm);
            // вывод текста
            result_text.setText("ППТ: " + round_it(ppt,2) + " м2\n"
            + "Масса миокарда ЛЖ: " + Math.round(mmlg) + " г\n"
            + "ИММ: " + Math.round(imm) + " г/м2\n"
            + compare_text);
        }
    }

    // метод очистки введенных значений при нажатии на кнопку Очистить
    public void clear_text(View view) {
        height.setText("");
        weight.setText("");
        tmgp.setText("");
        kdr.setText("");
        tzs.setText("");
        result_text.setText("");
    }

    // метод округления
    public static double round_it(double a, int b) {
        return Math.round(a * Math.pow(10, b)) / Math.pow(10, b);
    }

    // метод расчета ППТ(м2) = 0.007184 x рост (см)0.725 x вес (кг)0.425
    public static double calculate_ppt(double height, double weight) {
        return 0.007184 * Math.pow(height, 0.725)  * Math.pow(weight, 0.425);
    }

    // метод расчета Относительная толщина стенки (ОТC) = ТЗС * 2 / КДР
    public static double calculate_ots(double tzs, double kdr) {
        return 2 * tzs / kdr;
    }

    // метод расчета Масса миокарда ЛЖ (ММЛЖ) = 0,8 х 1,04 х [(МЖПд + КДР + ТЗСЛЖд)3 – КДР3] + 0,6
    public static double calculate_mmlg(double tmgp, double kdr, double tzs) {
        return (0.8 * 1.04 * (Math.pow(tmgp + kdr + tzs, 3) - Math.pow(kdr, 3)) + 0.6) / 1000;
    }

    // метод расчета Масса миокарда ЛЖ по ППТ (ИММ - индекс массы миокарда) (г/м2) = ММЛЖ/ППТ
    public static double calculate_imm(double mmlg, double ppt) {
        return mmlg / ppt;
    }

    // метод сравнения результов расчета
    public static String set_result_text(double imm_compared, double ots, double imm) {
        if (ots > 0.42) {
            if (imm > imm_compared) {
                return "Концентрическая гипертрофия";
            }
            else {
                return "Концентрическое ремоделирование";
            }
        }
        else {
            if (imm > imm_compared) {
                return "Эксцентрическая гипертрофия";
            }
            else {
                return "Нормальная геометрия";
            }
        }
    }
}