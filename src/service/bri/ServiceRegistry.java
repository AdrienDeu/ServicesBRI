package service.bri;

import service.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.List;
import java.util.Vector;


public class ServiceRegistry {
    // cette classe est un registre de services
    // partagée en concurrence par les clients et les "ajouteurs" de services,
    // un Vector pour cette gestion est pratique

    static {
        servicesClasses = new Vector<>();
        serviceState = new Vector<>();
    }
    private static final List<Class<? extends Service>> servicesClasses;
    private static final List<Boolean> serviceState;


    // ajoute une classe de service après contrôle de la norme BLTi
    public static void addService(Class<? extends Service> class1){

        Class<?>[] suprClass = new Class[]{class1.getSuperclass()};
        boolean impService = false;
        for (Class classe : suprClass) {
            if (classe.getName().equals("service.Service")) {
                impService = true;
                break;
            }
        }
        if (!impService) {
            throw new RuntimeException("La classe n'extends pas Service");
        }
        int modifier = class1.getModifiers();
        if(Modifier.isAbstract(modifier)) {
            throw new RuntimeException("Abstraite.");
        }
        if(!Modifier.isPublic(modifier)) {
            throw new RuntimeException("Pas publique.");
        }
        try {
            if(class1.getConstructor().getExceptionTypes().length!=0) {
                throw new RuntimeException("Le constructeur a des exceptions potentielles.");
            }
            if(!Modifier.isPublic(class1.getConstructor().getModifiers())) {
                throw new RuntimeException("Le constructeur n'est pas publique.");
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Field[] attributs = class1.getDeclaredFields();
        boolean hasSockPrivFinal = false;
        for (Field atr : attributs) {
            if (atr.getType() == java.net.Socket.class && Modifier.isFinal(atr.getModifiers()) && Modifier.isPrivate(atr.getModifiers())) {
                hasSockPrivFinal = true;
                break;
            }
        }
        if (!hasSockPrivFinal) {
            throw new RuntimeException("Pas d'atribut sock priv final");
        }
        Method[] metodes = class1.getMethods();
        boolean hasToStringue = false;
        for (Method meth : metodes) {
            if (meth.getName().equals("toStringue") && Modifier.isStatic(meth.getModifiers()) && Modifier.isPublic(meth.getModifiers()) && meth.getExceptionTypes().length == 0) {
                hasToStringue = true;
                break;
            }
        }
        if (!hasToStringue) {
            throw new RuntimeException("Pas de méthode toString");
        }


        servicesClasses.add(class1);

    }


    // renvoie la classe de service (numService -1)
    public static Class<? extends Service> getServiceClass(int numService) {
        if(serviceState.get(numService-1)) {
            return servicesClasses.get(numService-1);
        }
        return null;
    }

    public static Integer getServiceLength() {
        return servicesClasses.size();
    }
    public static void removeService(Integer numService) {

        servicesClasses.remove(numService-1);
        serviceState.remove(numService-1);
    }

    public static void UpdateStateService(Integer numService) {
        if(serviceState.get(numService-1)) {
            serviceState.set(numService-1, false);
        }
        else {
            serviceState.set(numService-1, true);
        }
    }



    // liste les activités présentes
    public static String toStringue() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        StringBuilder result = new StringBuilder();
        result.append("Activités présentes :");
        int i = 1;
        synchronized(servicesClasses) {
            for(Class<? extends Service> s : servicesClasses) {
                Method toStringue = s.getMethod("toStringue");
                String toStringueRes = (String) toStringue.invoke(s);

                if (serviceState.get(i-1)) {
                    result.append("\n").append(i).append(". ").append(toStringueRes).append(" : actif");
                }
                else {
                    result.append("\n").append(i).append(". ").append(toStringueRes).append(" : désactivé");
                }
                i++;


            }
        }
        return result.toString();
    }

    public static void addServiceState() {
        serviceState.add(true);
    }

    public static void deleteServiceState(Integer indice) {
        serviceState.remove(indice-1);
    }
}


