package com.example.andy.nessus_fenrir_callirrhoe;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.controllers.DatabaseController;
import com.example.controllers.NFCmanager;
import com.example.controllers.RequestAPI;
import com.example.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Andy on 17/04/2018.
 */

public class UserTest {

    @Mock
    private DatabaseReference db;

    @Mock
    private FirebaseDatabase fb;

    @Mock
    private ArrayList<String> list;

    @Mock
    private HashMap input;

    @Mock
    private HashMap output;

    @Before
    public void onMake() throws Exception{
        //MockitoAnnotations.initMocks(this);
        this.fb = Mockito.mock(FirebaseDatabase.class);
        this.db = Mockito.mock(DatabaseReference.class);
        this.list = Mockito.mock(ArrayList.class);
        this.input = Mockito.mock(HashMap.class);
        this.output = Mockito.mock(HashMap.class);

        //Mockito.when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(pref);
    }

    @Test
    public void toMapTest(){

        User test = mock(User.class);
        when(list.get(0)).thenReturn("foo");

        assertThat(test.toMap("foo0",list), CoreMatchers.<Map<String, Object>>is(output));
    }
}
