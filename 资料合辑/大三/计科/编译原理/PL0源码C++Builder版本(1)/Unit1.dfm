object Form1: TForm1
  Left = 105
  Top = 159
  Width = 1017
  Height = 767
  Align = alLeft
  Caption = 'Form1'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 832
    Top = 32
    Width = 160
    Height = 40
    Caption = 'Դ������'
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = '����'
    Font.Style = []
    ParentFont = False
  end
  object ButtonRun: TButton
    Left = 844
    Top = 395
    Width = 139
    Height = 54
    Caption = 'RUN'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = 'Times New Roman MT Extra Bold'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 0
    OnClick = ButtonRunClick
  end
  object Memo1: TMemo
    Left = 8
    Top = 8
    Width = 804
    Height = 729
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = 'Courier New'
    Font.Style = [fsBold]
    ImeName = '�Ϲ�ƴ�����뷨'
    Lines.Strings = (
      '***** PL/0 Compiler Demo *****')
    ParentFont = False
    ScrollBars = ssBoth
    TabOrder = 1
  end
  object EditName: TEdit
    Left = 831
    Top = 74
    Width = 162
    Height = 54
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = 'Courier New'
    Font.Style = [fsBold]
    ImeName = '�Ϲ�ƴ�����뷨'
    ParentFont = False
    TabOrder = 2
    Text = 'E01'
  end
  object ListSwitch: TRadioGroup
    Left = 824
    Top = 168
    Width = 177
    Height = 163
    Caption = 'Ŀ�����'
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = '����'
    Font.Style = []
    ItemIndex = 0
    Items.Strings = (
      '��ʾ'
      '����ʾ')
    ParentFont = False
    TabOrder = 3
  end
end
