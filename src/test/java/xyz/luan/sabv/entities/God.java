package xyz.luan.sabv.entities;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import xyz.luan.sabv.validations.Numeric.Max;
import xyz.luan.sabv.validations.Numeric.Min;
import xyz.luan.sabv.validations.Numeric.Natural;
import xyz.luan.sabv.validations.Required;

public class God {
    
    private String name;

    private Power[] powers;
    
    @Required private Weakness @Natural [] weaknesses;
    
    @Required private Weakness @Max(10) @Min(11) [] @Max(20) @Min(21) [] @Max(30) @Min(31) [] weaknessesMatrix;
    
    @Required private List<@Required Weakness> weaknessesList;
    
    @Required private List<@Min(21) List<@Max(12) Weakness>> weaknessesListMatrix;
    
    @Required private Map<@Required @Natural Integer, @Max(20) String> map;
    
    public static void main(String[] args) throws Throwable {
        Field matrix = God.class.getDeclaredField("weaknessesMatrix");
        
        System.out.println("ARRAY");
        printAll(matrix.getAnnotations());
        
        System.out.println();
        printAll(matrix.getAnnotatedType().getAnnotations());

        AnnotatedArrayType annotatedParameterizedType = (AnnotatedArrayType) matrix.getAnnotatedType();
        AnnotatedType at = annotatedParameterizedType.getAnnotatedGenericComponentType();
        System.out.println();
        printAll(at.getAnnotations());
        
        AnnotatedArrayType aat = (AnnotatedArrayType) at;
        System.out.println();
        printAll(aat.getAnnotatedGenericComponentType().getAnnotations());
        
        Field matrixList = God.class.getDeclaredField("weaknessesListMatrix");
        System.out.println();
        printAll(matrixList.getAnnotations());

        System.out.println("GENERICS");
        printAll(matrixList.getAnnotatedType().getAnnotations());

        AnnotatedParameterizedType apt = (AnnotatedParameterizedType) matrixList.getAnnotatedType();
        
        Arrays.asList(apt.getAnnotatedActualTypeArguments()).stream().forEach(t -> {
            System.out.println();
            printAll(t.getAnnotations());

            System.out.println();
            printAll(((AnnotatedParameterizedType) t).getAnnotatedActualTypeArguments()[0].getAnnotations());
        });
    }
    
    private static void printAll(Annotation... ann) {
        Arrays.asList(ann).stream().forEach(t -> System.out.println(t));
    }
    
    public God(String name, Power[] powers, Weakness[] weaknesses) {
        this.name = name;
        this.powers = powers;
        this.weaknesses = weaknesses;
        
        weaknesses = new @Required Weakness [12];
        weaknessesList = new ArrayList<@Required Weakness>();
    }

    public String getName() {
        return name;
    }

    public Power[] getPowers() {
        return powers;
    }

    public Weakness[] getWeaknesses() {
        return weaknesses;
    }
}
