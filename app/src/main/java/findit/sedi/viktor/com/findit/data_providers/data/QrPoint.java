package findit.sedi.viktor.com.findit.data_providers.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

// Как соответствующий тайник нашли, то бонусы его анулируются
// Если тайник не переиспользуетс
// При обнаружении тайника, у всех на карте появляется знак вопроса и его цвет в зависимости от сложности вопроса
// Пока тайник не найден, то ничего на карте не показываем
public class QrPoint {

    private long Bonus; // Бонусы которые получит пользователь
    private String Type; // Тип переиспользуемый или не переиспользуемый
    private String Mark; // Открыли, нашли, не нашли
    private long questBonus; // Бонус за одну отгаданную подсказку
    private HashMap<String, ArrayList<String>> quests; // Список с заданими (вопросами на которые должен ответить игрок)
    private String TipForNextQrPoint; // Подсказка где находится следующий тайник
    private String Tip; // подсказка где спрятан сам тайник
    private String TournamentID; // Турнир к которому принадлежит тайник
    private boolean isMain; // По умолчанию false говорит о  том что Этот ли главный тайник или нет?
    private String TipPhoto; // Фотография, подсказка на тайник
    private double Latitude;
    private double LongTitude;
    private double Distance;
    private long Difficulty; // Сложностьы
    private String ID;

    public QrPoint(long bonus, String type, String mark, long questBonus,
                   HashMap<String, ArrayList<String>> quests, String tipForNextQrPoint, String tip, String tournamentID,
                   boolean isMain, String tipPhoto, double latitude, double longTitude, double distance, long difficulty, String ID) {
        Bonus = bonus;
        Type = type;
        Mark = mark;
        this.questBonus = questBonus;
        this.quests = quests;
        TipForNextQrPoint = tipForNextQrPoint;
        Tip = tip;
        TournamentID = tournamentID;
        this.isMain = isMain;
        TipPhoto = tipPhoto;
        Latitude = latitude;
        LongTitude = longTitude;
        Distance = distance;
        Difficulty = difficulty;
        this.ID = ID;
    }

    public long getBonus() {
        return Bonus;
    }

    public void setBonus(long bonus) {
        Bonus = bonus;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMark() {
        return Mark;
    }

    public void setMark(String mark) {
        Mark = mark;

    }

    public LatLng getLatLong(){
        return  new LatLng(getLatitude(), getLongTitude());
    }


    public long getQuestBonus() {
        return questBonus;
    }

    public void setQuestBonus(long questBonus) {
        this.questBonus = questBonus;
    }

    public HashMap<String, ArrayList<String>> getQuests() {
        return quests;
    }

    public void setQuests(HashMap<String, ArrayList<String>> quests) {
        this.quests = quests;
    }

    public String getTipForNextQrPoint() {
        return TipForNextQrPoint;
    }

    public void setTipForNextQrPoint(String tipForNextQrPoint) {
        TipForNextQrPoint = tipForNextQrPoint;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public String getTournamentID() {
        return TournamentID;
    }

    public void setTournamentID(String tournamentID) {
        TournamentID = tournamentID;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getTipPhoto() {
        return TipPhoto;
    }

    public void setTipPhoto(String tipPhoto) {
        TipPhoto = tipPhoto;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongTitude() {
        return LongTitude;
    }

    public void setLongTitude(double longTitude) {
        LongTitude = longTitude;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public long getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(long difficulty) {
        Difficulty = difficulty;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
