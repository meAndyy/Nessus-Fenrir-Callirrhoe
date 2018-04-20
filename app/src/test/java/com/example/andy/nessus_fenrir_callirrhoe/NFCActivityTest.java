package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.controllers.NFCmanager;
import com.example.controllers.RequestAPI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andy on 17/04/2018.
 */

public class NFCActivityTest {

    @Mock
    private NFCmanager nfcMger;
    @Mock
    private SharedPreferences pref;

    @Mock
    private Context context;

    @Mock
    private HashMap init;

    @InjectMocks
    private NFCActivity activity;

    @Before
    public void onMake() throws Exception{
        this.init = Mockito.mock(HashMap.class);
        this.pref = Mockito.mock(SharedPreferences.class);
        this.context = Mockito.mock(Context.class);
        Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(pref);
    }

    @Test
    public void testToList(){

        NFCActivity test = mock(NFCActivity.class);
        List<String> list = new ArrayList<>();
        String[] s = {"dud@hotmail.com","dud1@hotmail.com","dud2@hotmail.com","dud3@hotmail.com"};
        HashMap<String, List<String>> map = init;
        for(int i = 0; i < s.length;i++){
           list.add(s[i]);
        }
        for(int i = 0; i < s.length;i++){
            map.put("foo"+i,list);
        }

        Mockito.when(pref.getString(anyString(), anyString())).thenReturn("curr_contact","DEFAULT");
        assertThat(test.toList(map), is(notNull()));
        assertArrayEquals(test.toList(map), eq(s));
    }

}
