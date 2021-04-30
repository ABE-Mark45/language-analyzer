package com.parser;

import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {


    @Test
    void parse() {
        assertThrows(Exception.class, () -> {
            Parser p = new Parser("z:=0");
            p.Parse().execute(p.variables);

        });
        assertThrows(Exception.class, () -> {
            Parser p = new Parser("x:=0");
            p.Parse().execute(p.variables);

        });

        // semi colon at the end
        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a:=0;b:=0;");
            p.Parse().execute(p.variables);
        });


        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a=1");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a:=-1");
            p.Parse().execute(p.variables);
        });


        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a:=");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("2");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("2;2");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("خ");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a:=0; b:=0 ; a:=b; a:=c; a:=d");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a:=0; b:=0 ; a:=b; a:=c; a:=d");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("a:=0; ;;;; a:=d");
            p.Parse().execute(p.variables);
        });

        assertThrows(Exception.class, () -> {
            Parser p = new Parser("if(a==0) then skip; else skip" +
                    "");
            p.Parse().execute(p.variables);
        });
    }
    @org.junit.jupiter.api.Test
    void test1() {

        assertDoesNotThrow(() -> {
            Parser p = new Parser("skip");
            p.Parse().execute(p.variables);
        });
        assertDoesNotThrow(()->{
            Parser p = new Parser("a:=10; b:=5; ccccc:=(a+b)");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("ccccc"), 15);
        });
        assertDoesNotThrow(()->{
            Parser p = new Parser("a:=0");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("a"), 0);
        });

        assertDoesNotThrow(()->{
            Parser p = new Parser("a:=0; b:=0 ; a:=b; a:=c; a:=d");
            p.Parse();
        });

        assertDoesNotThrow(()->{
            Parser p = new Parser("if tt then skip else skip");
            p.Parse();
        });

        assertDoesNotThrow(()->{
            Parser p = new Parser("a:=0;  skip ; skip" +
                    " ; skip ; a:=0 ; b:=0 ; a:=b; c:=1");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("c"), 1);
        });

        assertDoesNotThrow(()->{
            System.out.println("---");
            Parser p = new Parser("" +
                    "c:=0;" +
                    "if ff then" +
                    "c:=1" +
                    "else" +
                    "c:=2" +
                    "" +
                    "");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("c"), 2);
        });

        assertDoesNotThrow(() -> {
            Parser p = new Parser("a:=(a+1)");
            p.Parse();
        });
    }
    @org.junit.jupiter.api.Test
    void ifElseTest() {
        assertDoesNotThrow(()->{
            Parser p = new Parser(
                    "c:=0;" +
                            "c:=(c+1);" +
                            "a:=0; b:=10;" +
                            "if(a==0) then" +
                            "b:=(b+1);" +
                            "d:=0;" +
                            "d:=1;" +
                            "if(d == 5)" +
                            "then" +
                            "d:=4"+
                            "else" +
                            "d:=10;" +
                            "aa:=1234;" +
                            "if tt then" +
                            "aa:=3" +
                            "else" +
                            "aa:=2" +
                            "else" +
                            "b:=(b-1)" +
                            ""
            );
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 11);
            assertEquals(p.variables.variables.get("c"), 1);
            assertEquals(p.variables.variables.get("d"), 10);
            assertEquals(p.variables.variables.get("aa"), 3);
        });


        assertDoesNotThrow(() -> {
            Parser p = new Parser("a:=0;\n" +
                    "if ( ((a == 0) ^ (a == 0)) ^ (a == 0) ) " +
                    "then b := 5 else b := 6");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 5);
        });
        assertDoesNotThrow(() -> {
            Parser p = new Parser("a:=0;\n" +
                    "        if ( ( (a == 0) ^ (a == 0) ) ^ ( (a == 0) ^ (a == 0) ) ) " +
                    "then b := 5 else b := 6");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 5);
        });
        assertDoesNotThrow(() -> {
            Parser p = new Parser("a:=0;\n" +
                    "        if ((a == 0) ^ ((a == 0) ^ (a == 0))) then b := 5 else b := 6\n");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 5);
        });
        assertDoesNotThrow(() -> {
            Parser p = new Parser(" a:= 1;\n" +
                    "        b := ((6-a)+(5 + a))");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 11);
        });
        assertDoesNotThrow(() -> {
            Parser p = new Parser("  a:= 1; b:=2;" +
                    "b := ((6-a)+( ( (b+1) +5) + (1+b) ))");
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 16);
        });

    }


    @org.junit.jupiter.api.Test
    void AddFromToN(){
        File file = new File(new File("").getAbsoluteFile() +
                File.separator + "com" + File.separator + "parser" +
                File.separator + "examples" + File.separator + "example1.txt");

        assertDoesNotThrow(() -> {
            Parser p = new Parser( new StreamProvider( new FileInputStream(file)) );
            p.Parse().execute(p.variables);
            assertEquals(p.variables.variables.get("b"), 55);
        });
    }


    @org.junit.jupiter.api.Test
    void Fibonacci() {
        File file = new File(new File("").getAbsoluteFile() +
                File.separator + "com" + File.separator + "parser" +
                File.separator + "examples" + File.separator + "example2.TN");

        assertDoesNotThrow(() -> {
            Parser p = new Parser( new StreamProvider( new FileInputStream(file)) );
            p.Parse().execute(p.variables);
            System.out.println(p.variables.variables.get("a"));

            System.out.println(p.variables.variables.get("b"));
            System.out.println(p.variables.variables.get("c"));
            assertEquals(p.variables.variables.get("a"), 55);
            assertEquals(p.variables.variables.get("b"), 89);
            assertEquals(p.variables.variables.get("c"), 144);
        });
    }

}
