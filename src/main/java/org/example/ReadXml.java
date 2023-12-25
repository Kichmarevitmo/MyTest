package org.example;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;

public class ReadXml {
    private static ArrayList<AS_ADDR_OBJ_Object> addressObjects = new ArrayList<>();
    private static ArrayList<AS_ADM_HIERARCHY_Object> hierarchyObjects = new ArrayList<>();
    /* понять различие между == и equals */

    public static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("OBJECT")) {
                String objectId = attributes.getValue("OBJECTID");
                String name = attributes.getValue("NAME");
                String typeName = attributes.getValue("TYPENAME");
                String startDate = attributes.getValue("STARTDATE");
                String endDate = attributes.getValue("ENDDATE");
                String isActual = attributes.getValue("ISACTUAL");
                String isActive = attributes.getValue("ISACTIVE");
                addressObjects.add(new AS_ADDR_OBJ_Object(objectId, name, typeName, startDate, endDate, isActual, isActive));
            } else if (qName.equals("ITEM")) {
                String objectId = attributes.getValue("OBJECTID");
                String parentObjectId = attributes.getValue("PARENTOBJID");
                hierarchyObjects.add(new AS_ADM_HIERARCHY_Object(objectId, parentObjectId));
            }
        }
    }

    public static List<String> getAddress(String date, List<String> objectIds) {
        // создание пустого списка
        List<String> address = new ArrayList<>();
        for (AS_ADDR_OBJ_Object obj : addressObjects) {
            String startDate = obj.getStartDate();
            String endDate = obj.getEndDate();
            if (isDateInRange(startDate, endDate, date) && objectIds.contains(obj.getObjectId())) {
                address.add(obj.getObjectId() + ": " + obj.getTypeName() + " " + obj.getName());
            }
        }
        if (address.isEmpty()) {
            address.add("Ничего не найдено.");
        }
        return address;
    }

    /*
      В Java две даты можно сравнить, используя метод compareTo() интерфейса Comparable.
      Этот метод возвращает '0', если обе даты равны, возвращает значение "больше 0",
      если date1 идет после date2, и возвращает значение "меньше 0",
      если date1 идет до date2.
      + есть еще способ сравнения через equals а также через before и after
     */
    private static boolean isDateInRange(String startDate, String endDate, String targetDate) {
        return startDate.compareTo(targetDate) <= 0 && endDate.compareTo(targetDate) >= 0;
    }

    public static List<String> getAddressChainsById() {
        List<String> addressChains = new ArrayList<>();

        for (AS_ADDR_OBJ_Object obj : addressObjects) {
            if (obj.getTypeName().contains("проезд")) {
                List<String> addressChain = buildAddressChain(obj);
                Collections.reverse(addressChain);  // Обратный порядок
                if (!addressChain.isEmpty()) {
                    addressChains.addAll(addressChain);
                    addressChains.add("\n");
                }
            }
        }

        if (addressChains.isEmpty()) {
            addressChains.add("Ничего не найдено.");
        }

        return addressChains;
    }

    private static List<String> buildAddressChain(AS_ADDR_OBJ_Object obj) {
        List<String> addressChain = new ArrayList<>();
        addressChain.add(obj.getTypeName() + " " + obj.getName());

        String currentObjectId = obj.getObjectId();
        String parentObjectId;

        while ((parentObjectId = getParentObjectId(currentObjectId)) != null) {
            AS_ADDR_OBJ_Object parentObject = findObjectById(parentObjectId);

            if (parentObject != null && parentObject.getIsActual().equals("1") && parentObject.getIsActive().equals("1")) {
                addressChain.add(parentObject.getTypeName() + " " + parentObject.getName());
                currentObjectId = parentObject.getObjectId();
            } else {
                break;
            }
        }

        return addressChain;
    }

    private static String getParentObjectId(String objectId) {
        for (AS_ADM_HIERARCHY_Object hierarchyObject : hierarchyObjects) {
            if (hierarchyObject.getObjectId().equals(objectId) ) {
                return hierarchyObject.getParentObjectId();
            }
        }
        return null;
    }

    private static AS_ADDR_OBJ_Object findObjectById(String objectId) {
        for (AS_ADDR_OBJ_Object obj : addressObjects) {
            if (obj.getObjectId().equals(objectId)) {
                return obj;
            }
        }
        return null;
    }

}
/*
1 шаг: В методе startElement мы читаем данные из файла и создаем два списка
addressObjects - адреса
hierarchyObjects - хранит информацию об иерахии
2 шаг: Запускаем getAddressChainsById
public static List<String> getAddressChainsById() {
        List<String> addressChains = new ArrayList<>();

        for (AS_ADDR_OBJ_Object obj : addressObjects) { // в этом цикле среди адресов мы ищем у кого тип "проезд"
        и после того как мы нашли то запускаем метод который будет создавать цепочку адресов (полный адрес)
            if (obj.getTypeName().contains("проезд")) {
                List<String> addressChain = buildAddressChain(obj);
                Collections.reverse(addressChain);  // Обратный порядок
                if (!addressChain.isEmpty()) {
                    addressChains.addAll(addressChain);
                    addressChains.add("\n");
                }
            }
        }

        if (addressChains.isEmpty()) {
            addressChains.add("Ничего не найдено.");
        }

        return addressChains;
}
3 шаг : вызов метода buildAddressChain
private static List<String> buildAddressChain(AS_ADDR_OBJ_Object obj) {
        List<String> addressChain = new ArrayList<>(); // создаем список для цепочки пустой
        addressChain.add(obj.getTypeName() + " " + obj.getName()); // сразу добавляем в цепочку корень
        String currentObjectId = obj.getObjectId(); // задаем текущий ребенок это текущий объект (корень)
        String parentObjectId;
        // в цикле проходимся до тех пор пока у нас родитель не будет равен null.
        // то есть до тех пор пока мы его не найдем
        while ((parentObjectId = getParentObjectId(currentObjectId)) != null) {
            // находим текущего родителя имеется ввиду что у нас идентификатор родителя, но

            AS_ADDR_OBJ_Object parentObject = findObjectById(parentObjectId);
            // если родитель найдем и он актуальный то мы добавляем его в цепочку и говорим
            // что он текущий ребенок, иначе выходим из цикла
            if (parentObject != null && parentObject.getIsActual().equals("1") && parentObject.getIsActive().equals("1")) {
                addressChain.add(parentObject.getTypeName() + " " + parentObject.getName());
                currentObjectId = parentObject.getObjectId();
            } else {
                break;
            }
        }

        return addressChain;
    }
 */