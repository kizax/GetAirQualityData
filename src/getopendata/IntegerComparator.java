/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.util.Comparator;

/**
 *
 * @author kizax
 */
public class IntegerComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer o1,
            Integer o2) {
        return (int) (o1 - o2);
    }
}
