package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.controllers.NFCmanager;
import com.example.controllers.RequestAPI;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;

/**
 * Created by Andy on 17/04/2018.
 */

public class RequestAPITest {

    @Mock
    private RequestAPI api;

    @Before
    public void onMake() throws Exception{
        //MockitoAnnotations.initMocks(this);
         this.api = Mockito.mock(RequestAPI.class);
        //this.pref = Mockito.mock(SharedPreferences.class);
        //this.context = Mockito.mock(Context.class);
        //Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(pref);
    }

    @Test
    public void testSendData(){

        RequestAPI test = mock(RequestAPI.class);
        //HashMap<String, List<String>> init = new HashMap<>();
        //Mockito.when(pref.getString(anyString(), anyString())).thenReturn("curr_contact","DEFAULT");
        assertThat(test.sendData("andyr123436@gmail.com"), is(true));
        //assertArrayEquals(test.toList(map), eq(s));
    }
}
