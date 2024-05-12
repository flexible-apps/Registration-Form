package com.flexible.launcher;

import com.flexible.assets.MFAAssertBuilder;
import com.flexible.implementions.MainLoader;

@MainLoader
public class Console {
    public static void main(String[] args) {
        MFAAssertBuilder.notNull("", s -> System.out.println(""));
    }
}
