package com.fdoom.utils;

import java.io.File;
import java.util.ArrayList;
import com.fdoom.Game;
import com.fdoom.GameContainer;

import java.util.List;
import java.util.Scanner;
import java.util.Formatter;

public class SystemFile
{
    public boolean applet;
    public String FileName;
    private Formatter formatter;
    private Scanner scanner;
    public String Text;
    public List<String> Lines;
    private int filePos;
    private boolean end;
    public boolean isNew;
    
    public SystemFile(final String s, final Game game) {
        this.Lines = new ArrayList<String>();
        this.FileName = s;
        this.Text = "";
        this.filePos = 0;
        this.end = false;
        this.isNew = false;
    }
    
    public void Create() {
        if (this.applet) {
            return;
        }
        //System.out.println("Create File: " + this.FileName);
        final File file = new File(this.FileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                this.isNew = true;
            }
            catch (Exception e) {
                System.out.println("You have an error! -Create()");
                GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in SystemFile - Line 46. Please send to Dev!");
            }
        }
    }
    
    public void Reset() {
        if (this.applet) {
            return;
        }
        try {
            this.scanner = new Scanner(new File(this.FileName));
        }
        catch (Exception e) {
            System.out.println("You have an error! -Reset()");
            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in SystemFile - Line 60. Please send to Dev!");
        }
        this.filePos = 0;
        this.end = false;
    }
    
    public void Rewrite() {
        if (this.applet) {
            return;
        }
        try {
            this.formatter = new Formatter(this.FileName);
        }
        catch (Exception e) {
            System.out.println("You have an error! -Rewrite()");
            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in SystemFile - 75. Please send to Dev!");
        }
        this.filePos = 0;
        this.end = false;
    }
    
    public void Read() {
        if (this.applet) {
            return;
        }
        if (this.scanner != null) {
            this.Text = "";
            int i = 0;
            while (this.scanner.hasNext()) {
                this.Lines.add(this.scanner.next());
                if (i > 0) {
                    this.Text = String.valueOf(this.Text) + " ";
                }
                this.Text = String.valueOf(this.Text) + this.Lines.get(i);
                ++i;
            }
        }
    }
    
    public String ReadLn() {
        if (this.applet) {
            return "";
        }
        String s = "0";
        if (this.scanner != null) {
            this.Read();
            if (this.filePos < this.Lines.size()) {
                s = this.Lines.get(this.filePos);
                ++this.filePos;
            }
            else {
                this.end = true;
            }
        }
        if (this.filePos == this.Lines.size()) {
            this.end = true;
        }
        return s;
    }
    
    public void Write() {
        if (this.applet) {
            return;
        }
        if (this.formatter != null) {
            for (int i = 0; i < this.Lines.size(); ++i) {
                if (i > 0) {
                    this.Text = String.valueOf(this.Text) + " ";
                }
                this.Text = String.valueOf(this.Text) + this.Lines.get(i);
            }
            this.formatter.format("%s", this.Text);
        }
    }
    
    public void WriteLn(final String s) {
        if (this.applet) {
            return;
        }
        if (this.formatter != null) {
            this.Lines.add(s);
            ++this.filePos;
        }
    }
    
    public boolean EndOfFile() {
        return !this.applet && this.end;
    }
    
    public void Close() {
        if (this.applet) {
            return;
        }
        if (this.formatter != null) {
            this.formatter.close();
        }
        if (this.scanner != null) {
            this.scanner.close();
        }
    }
}
