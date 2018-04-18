package drag;
/*
Просто подведя курсор мыши к объекту и нажав кнопку, Вы тем самым
выделили объект. Теперь перемещая мышь с нажатой клавишей,
Вы перемещаете и объект.
Как только Вы выбираете объект, он автоматически привязывается своим
верхним левым углом к указателю мыши. Теперь в действие вступает
обработчик событий мыши mouseDrag() - перемещение мыши
при нажатой клавише.
также предусмотрено ограничение при попытке выйти за границы .
Более детальное описание смотрите в тексте примера.
 */

import java.applet.*;
import java.awt.*;

//========================================
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

class Coordinate // Для начала в программе объявляется новый класс Coordinate,
//  который  содержит координаты x и y. Используя обьекты класса Coordinate,
// можно хранить координаты объекта. Функция-конструктор класса использует
// значения его параметров для ввода переменных координат:
{

    public int x;
    public int y;

    public Coordinate(int x, int y) {
// Параметры функции-конструктора тоже называются х и у, как и переменные-
// члены класса. Для различия между ними используется ключевое слово this.
// Функцию-конструктор можно было бы описать и так:
//  public Coordinate(int a, int b)
//           {
//            x = a;
// 	      y = b;
//	     }

        this.x = x;
        this.y = y;
    }
}
//============================================
public class Drag extends Applet //implements Runnable
{

    Graphics g;
    Image background; // Здесь будет храниться картинка фона
    Image home; // В этой переменной будет содержаться объект перемещения
    Coordinate coordinate;
    boolean done_loading = false;
    boolean selection = false;
    int width; // Данные переменные хранят ширину и
    int height; //  высоту фонового изображения
    int new_x;
    int new_y;

    //=====================================================================
    public void init() {
        try {

            g = getGraphics();
            // Получение изображения для фона и объекта из файлов
            background = getImage(new URL("file:///I:/TemplateCourse/resource/practice_2/variant_6/sat.jpg"));
            home = getImage(new URL("file:///I:/TemplateCourse/resource/practice_2/variant_6/mir.gif"));

            width = size().width;
            height = size().height;


            get_coordinate();


            // Создается изображение вне экрана ( createImage), а
            // затем выводится на экран функцией drawImage.
            Image offScrImage = createImage(width, height);
            Graphics offScrGC = offScrImage.getGraphics();
            offScrGC.drawImage(background, 0, 0, width, height, this);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Drag.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    //==========================================================
    void get_coordinate() {
        // Начальное положение объекта
        coordinate = new Coordinate(100, 100);
    }

    //========================================================
    public void move_s() {

        int old_x = coordinate.x;//Сохранение координат предыдущего (" старого")
        int old_y = coordinate.y;// местоположени


        Graphics g2;
        g2 = g.create();

        // Затем clipRect создает  прямоугольник отсечения ( там где раньше
        // было изображение объекта )
        g2.clipRect(old_x, old_y, home.getWidth(this), home.getHeight(this));

        // Если "новое" изображение объекта попадает полностью в границы
        // апплета, то новые координаты становяться текущими. Затем восстанавливается
        // фоновое изображение и выводится объект уже в новом месте с координатами
        // new_x и new_y
        if (((new_x > 0) & (new_x < (width - home.getWidth(this)))) & ((new_y > 0) & (new_y < (height - home.getHeight(this))))) {
            coordinate.x = new_x;
            coordinate.y = new_y;
            g2.drawImage(background, 0, 0, null);
            g.drawImage(home, new_x, new_y, this);
            repaint();
        }
    }
    //=====================================================================
    public boolean mouseDown(Event evt, int x, int y) // Если указатель мыши помещается в заданную область аплета,
    // и нажимается любая клавиша мыши,
    // то  определяется: принадлежит ли данная точка объекту или это фон,
    // Если принадлежит, то selection становится истиной до тех пор,
    // пока клавишу не отпустят.
    {
        if (x > coordinate.x && x < (coordinate.x + home.getWidth(this)) && y > coordinate.y && y < (coordinate.y + home.getHeight(this))) {
            selection = true;
        }

        return true;
    }
    //======================================================================
    public boolean mouseUp(Event evt, int x, int y)// Отпускание клавиши
    {
        selection = false;
        return true;
    }

    //======================================================================
    // Перемещение мыши с нажатой клавишей
    public boolean mouseDrag(Event evt, int x, int y) {
        if (selection) // Если истина, то вызывается move_s()
        {
            new_x = x;  // Координаты мышки привязываются к верхнему
            new_y = y;  // левому углу картинки
            move_s();
        }
        return true;
    }

    //======================================================================
    public boolean imageUpdate(Image img, int infoflags, int x, int y,
                               int w, int h) {
        // Пока фоновое изображение загружается, параметр
        // infoflags не равен ALLBITS. Переменная done_loading
        // равна false, и в строке статуса Вы видите Loading
        if (infoflags == ALLBITS) {
            done_loading = true;
            repaint();

            return false;
        } else {
            return true;
        }
    }

    //======================================================
    public void paint(Graphics _g) {
        if (!done_loading) {
            showStatus("Loading");
        } else {
            showStatus("OK");
            g.drawImage(background, 0, 0, this);
            g.drawImage(home, coordinate.x, coordinate.y, this);
        }
    }
//==============================================
}