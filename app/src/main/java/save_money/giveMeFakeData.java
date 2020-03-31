package save_money;

import com.example.intern.SocialNetwork.PersonDetails_pojo;

import java.util.ArrayList;

public class giveMeFakeData {

    public static ArrayList<Offers_Pojo> giveMeFakeData(){
        ArrayList<Offers_Pojo> offersPojoArrayList = new ArrayList<>();

        offersPojoArrayList.add(new Offers_Pojo("1","Jyoti Dental Clinic","Paldi","50% OFF + 15% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Dhawanil Dental Clinic","Satellite","50% OFF + 20% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Aashray Dental Clinic","Paldi","50% OFF + 20% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Hardik Shah Dental Clinic","Anjali","200Rs. OFF + 10% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Ved Dental Clinic","Anjali","300Rs. OFF + 10% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Ved Dental Clinic","Anjali","300Rs. OFF + 10% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Ved Dental Clinic","Anjali","300Rs. OFF + 10% on Second Onwards"));
        offersPojoArrayList.add(new Offers_Pojo("1","Ved Dental Clinic","Anjali","300Rs. OFF + 10% on Second Onwards"));

        return offersPojoArrayList;
    }

    public static  ArrayList<Offers_Pojo> giveMeFakeData_eyeCLinic(){
        ArrayList<Offers_Pojo> offersPojosList = new ArrayList<>();
        offersPojosList.add(new Offers_Pojo("1","Jyoti Eye Clinic","Paldi","50% OFF + 15% on Second Onwards"));
        offersPojosList.add(new Offers_Pojo("1","Sanjivini  Eye Clinic","Satellite","50% OFF + 20% on Second Onwards"));
        offersPojosList.add(new Offers_Pojo("1","Raghudeep Eye Clinic","Paldi","50% OFF + 20% on Second Onwards"));
        return offersPojosList;
    }

    public static  ArrayList<Offers_Pojo> giveMeFakeData_dietician(){
        ArrayList<Offers_Pojo> offersPojosList = new ArrayList<>();
        offersPojosList.add(new Offers_Pojo("1","Kalpna Shukla Dietician ","Vastrapur","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Abha Dietician","Satellite","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Hetal Dietician","Paldi","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Someshwar Dietician","Vejalpur","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Kalpna Shukla Dietician ","Vastrapur","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Abha Dietician","Satellite","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Hetal Dietician","Paldi","Fist Consulatation Free"));
        offersPojosList.add(new Offers_Pojo("1","Someshwar Dietician","Vejalpur","Fist Consulatation Free"));
        return offersPojosList;
    }

    public static  ArrayList<Offers_Pojo> giveMeFakeData_homeopathy(){          //return type - ArrayList<Offers_Pojo>
        ArrayList<Offers_Pojo> offersPojosList = new ArrayList<>();
        offersPojosList.add(new Offers_Pojo("1","Diviyam Homepathy","Paldi","70% OFF + 30% on Second Onwards"));
        offersPojosList.add(new Offers_Pojo("1","Spandan Homepathy","Satellite","70% OFF + 30%  on Second Onwards"));
        return offersPojosList;
    }

    public static ArrayList<PersonDetails_pojo> givefakePeopleDetails(){
        ArrayList<PersonDetails_pojo> personDetails_pojoArrayList = new ArrayList<>();
        personDetails_pojoArrayList.add(new PersonDetails_pojo("1","Ashu","Business","50"));    // calling the constructor of pojoClass
        personDetails_pojoArrayList.add(new PersonDetails_pojo("1","Pransh","CEO","20"));
        personDetails_pojoArrayList.add(new PersonDetails_pojo("1","Amishi","Manager","18"));
        personDetails_pojoArrayList.add(new PersonDetails_pojo("1","Ramnish","Founder CEO","52"));
        return  personDetails_pojoArrayList ;
    }
}
