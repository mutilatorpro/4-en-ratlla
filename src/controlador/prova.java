/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/**
 *
 * @author inmad
 */
public class prova {
    public static void main(String[] args) {
        int[][] aux = new int[7][8];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                aux[i][j] = j + i * 7 + 1;
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(aux[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }
}
