package com.tobe.fishking.v2.addon;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Properties;

public class KSPayApprovalCancelBean {

    private static final String MSG_ENCODING = "ksc5601";

    private String IPAddr;
    private int Port;

    private KSPaySocketBean KSPaySocket;

    public String HeadMsg;            //Head Message
    public String DataMsg;
    public String SendMsg;
    public String ReceiveMsg;

    public int SendCount = 0, ReceiveCount = 0;

    private int MAXSIZE = 9;

    /* Haeder */
    public String EncType,                            // 0: ��ȭ����, 1:openssl, 2: seed
            Version,                            // ��������
            Type,                            // ����
            Resend,                            // ���۱��� : 0 : ó��,  2: ������
            RequestDate,                            // ��û���� : yyyymmddhhmmss
            StoreId,                            // �������̵�
            OrderNumber,                            // �ֹ���ȣ
            UserName,                            // �ֹ��ڸ�
            IdNum,                            // �ֹι�ȣ or ����ڹ�ȣ
            Email,                            // email
            GoodType,                            // ��ǰ���� 1 : �ǹ�, 2 : ������
            GoodName,                            // ��ǰ��
            KeyInType,                            // KeyInType ���� : S : Swap, K: KeyInType
            LineType,                            // lineType 0 : offline, 1:internet, 2:Mobile
            PhoneNo,                            // �޴�����ȣ
            ApprovalCount,                            // ���ս��ΰ���
            HeadFiller;                            // ����

    /* �ſ�ī����ΰ�� */
    public String[] ApprovalType = new String[MAXSIZE],      // ���α���
            TransactionNo = new String[MAXSIZE],      // �ŷ���ȣ
            Status = new String[MAXSIZE],      // ���� O : ���� , X : ����
            TradeDate = new String[MAXSIZE],      // �ŷ�����
            TradeTime = new String[MAXSIZE],      // �ŷ��ð�
            IssCode = new String[MAXSIZE],      // �߱޻��ڵ�
            AquCode = new String[MAXSIZE],      // ���Ի��ڵ�
            AuthNo = new String[MAXSIZE],      // ���ι�ȣ or ������ �����ڵ�
            Message1 = new String[MAXSIZE],      // �޽���1
            Message2 = new String[MAXSIZE],      // �޽���2
            CardNo = new String[MAXSIZE],      // ī���ȣ
            ExpDate = new String[MAXSIZE],      // ��ȿ�Ⱓ
            Installment = new String[MAXSIZE],      // �Һ�
            Amount = new String[MAXSIZE],      // �ݾ�
            MerchantNo = new String[MAXSIZE],      // ��������ȣ
            AuthSendType = new String[MAXSIZE],      // ���۱���= new String[MAXSIZE]
            ApprovalSendType = new String[MAXSIZE],      // ���۱���(0 : ����, 1 : ����, 2: ��ī��)
            Point1 = new String[MAXSIZE],
            Point2 = new String[MAXSIZE],
            Point3 = new String[MAXSIZE],
            Point4 = new String[MAXSIZE],
            VanTransactionNo = new String[MAXSIZE],      // Van �ŷ���ȣ
            Filler = new String[MAXSIZE],      // ����
            AuthType = new String[MAXSIZE],      // ISP : ISP�ŷ�, MP1, MP2 : MPI�ŷ�, SPACE : �Ϲݰŷ�
            MPIPositionType = new String[MAXSIZE],      // K : KSNET, R : Remote, C : ��3���, SPACE : �Ϲݰŷ�
            MPIReUseType = new String[MAXSIZE],      // Y : ����, N : ����ƴ�
            EncData = new String[MAXSIZE];      // MPI, ISP ������

    /* ������½��ΰ�� */
    public String[]
            VATransactionNo = new String[MAXSIZE],
            VAStatus = new String[MAXSIZE],
            VATradeDate = new String[MAXSIZE],
            VATradeTime = new String[MAXSIZE],
            VABankCode = new String[MAXSIZE],
            VAVirAcctNo = new String[MAXSIZE],
            VAName = new String[MAXSIZE],
            VACloseDate = new String[MAXSIZE],
            VACloseTime = new String[MAXSIZE],
            VARespCode = new String[MAXSIZE],
            VAMessage1 = new String[MAXSIZE],
            VAMessage2 = new String[MAXSIZE],
            VAAmount = new String[MAXSIZE],
            VAFiller = new String[MAXSIZE];

    /* �����н����ΰ�� */
    public String[]
            WPTransactionNo = new String[MAXSIZE],
            WPStatus = new String[MAXSIZE],
            WPTradeDate = new String[MAXSIZE],
            WPTradeTime = new String[MAXSIZE],
            WPIssCode = new String[MAXSIZE],      // �߱޻��ڵ�
            WPAuthNo = new String[MAXSIZE],      // ���ι�ȣ
            WPBalanceAmount = new String[MAXSIZE],      // �ܾ�
            WPLimitAmount = new String[MAXSIZE],      // �ѵ���
            WPMessage1 = new String[MAXSIZE],      // �޽���1
            WPMessage2 = new String[MAXSIZE],      // �޽���2
            WPCardNo = new String[MAXSIZE],      // ī���ȣ
            WPAmount = new String[MAXSIZE],      // �ݾ�
            WPMerchantNo = new String[MAXSIZE],      // ��������ȣ
            WPFiller = new String[MAXSIZE];      // ����

    /* ����Ʈī����ΰ�� */
    public String[]
            PTransactionNo = new String[MAXSIZE],      // �ŷ���ȣ
            PStatus = new String[MAXSIZE],      // ���� O : ���� , X : ����
            PTradeDate = new String[MAXSIZE],      // �ŷ�����
            PTradeTime = new String[MAXSIZE],      // �ŷ��ð�
            PIssCode = new String[MAXSIZE],      // �߱޻��ڵ�
            PAuthNo = new String[MAXSIZE],      // ���ι�ȣ or ������ �����ڵ�
            PMessage1 = new String[MAXSIZE],      // �޽���1
            PMessage2 = new String[MAXSIZE],      // �޽���2
            PPoint1 = new String[MAXSIZE],      // �ŷ�����Ʈ
            PPoint2 = new String[MAXSIZE],      // ��������Ʈ
            PPoint3 = new String[MAXSIZE],      // ��������Ʈ
            PPoint4 = new String[MAXSIZE],      // ����������Ʈ
            PMerchantNo = new String[MAXSIZE],      // ��������ȣ
            PNotice1 = new String[MAXSIZE],      //
            PNotice2 = new String[MAXSIZE],      //
            PNotice3 = new String[MAXSIZE],      //
            PNotice4 = new String[MAXSIZE],      //
            PFiller = new String[MAXSIZE];      // ����

    /* ���ݿ��������ΰ�� */
    public String[]
            HTransactionNo = new String[MAXSIZE],    // �ŷ���ȣ
            HStatus = new String[MAXSIZE],    // �������� O:���� X:����
            HCashTransactionNo = new String[MAXSIZE],    // ���ݿ����� �ŷ���ȣ
            HIncomeType = new String[MAXSIZE],    // 0: �ҵ�      1: ��ҵ�
            HTradeDate = new String[MAXSIZE],    // �ŷ� ���� ����
            HTradeTime = new String[MAXSIZE],    // �ŷ� ���� �ð�
            HMessage1 = new String[MAXSIZE],    // ���� message1
            HMessage2 = new String[MAXSIZE],    // ���� message2
            HCashMessage1 = new String[MAXSIZE],    // ����û �޽��� 1
            HCashMessage2 = new String[MAXSIZE],    // ����û �޽��� 2
            HFiller = new String[MAXSIZE];    // ����

    /*�ڵ��� ����1�� ���ΰ��*/
    public String[]
            MB1ApprovalType = new String[MAXSIZE],      /* �����ڵ� */
            MB1TransactionNo = new String[MAXSIZE],      /* �ŷ���ȣ */
            MB1Status = new String[MAXSIZE],      /* ���� : O, X */
            MB1TradeDate = new String[MAXSIZE],      /* �ŷ����� */
            MB1TradeTime = new String[MAXSIZE],      /* �ŷ��ð� */
            MB1Serverinfo = new String[MAXSIZE],      /* ����INFO : ��ü������ ���� �ʿ� ���� ����� */
            MB1Smsval = new String[MAXSIZE],      /* �ٳ��� ��� space */
            MB1Stanrespcode = new String[MAXSIZE],      /* �����ڵ� */
            MB1Message = new String[MAXSIZE],      /* �����޽��� */
            MB1Filler = new String[MAXSIZE];

    /*�ڵ��� ����2�� ���ΰ��*/
    public String[]
            MB2ApprovalType = new String[MAXSIZE],      /* �����ڵ� */
            MB2TransactionNo = new String[MAXSIZE],      /* �ŷ���ȣ */
            MB2Status = new String[MAXSIZE],      /* ����     */
            MB2TradeDate = new String[MAXSIZE],      /* �ŷ����� */
            MB2TradeTime = new String[MAXSIZE],      /* �ŷ��ð� */
            MB2Stanrespcode = new String[MAXSIZE],      /* �����ڵ� */
            MB2Message = new String[MAXSIZE],      /* ����޽��� */
            MB2Filler = new String[MAXSIZE];      /* ���� */

    // �޴����������
    public String[]
            MTransactionNo = new String[MAXSIZE],      // �ŷ���ȣ
            MStatus = new String[MAXSIZE],      // �������� O:���� X:����
            MTradeDate = new String[MAXSIZE],      // �ŷ� ����
            MTradeTime = new String[MAXSIZE],      // �ŷ� �ð�
            MBalAmount = new String[MAXSIZE],      // �ܾ�
            MRespCode = new String[MAXSIZE],      // �����ڵ�
            MRespMsg = new String[MAXSIZE],      // �ŷ� ���� �ð�
            MBypassMsg = new String[MAXSIZE],      // Echo�׸�
            MCompCode = new String[MAXSIZE],      // ��ü�ڵ�
            MTid = new String[MAXSIZE],      // ����������ü ���ι�ȣ
            MCommSele = new String[MAXSIZE],      // SKT,KTF,LGT
            MMobileNo = new String[MAXSIZE],      // �޴�����ȣ
            MApprAmt = new String[MAXSIZE],      // ���αݾ�
            MFiller = new String[MAXSIZE];      // ����

    /*ī�� BIN check ���ΰ��*/
    public String[]
            BINTransactionNo = new String[MAXSIZE],      /* �ŷ���ȣ					  */
            BINStatus = new String[MAXSIZE],      /* �������� => O:���� X:����	  */
            BINTradeDate = new String[MAXSIZE],      /* �ŷ� ����(YYYYMMDD)           */
            BINTradeTime = new String[MAXSIZE],      /* �ŷ� �ð�(HHMMSS)             */
            BINAquCode = new String[MAXSIZE],      /* �߱ޱ���ڵ�                  */
            BINMessage1 = new String[MAXSIZE],      /* ���� message1                 */
            BINMessage2 = new String[MAXSIZE],      /* ���� message2                 */
            BINAuthType = new String[MAXSIZE],      /* 'I':ISP, 'M':MPI, ' ':��Ÿ    */
            BINMpiLoc = new String[MAXSIZE],      /* MPI�����ġ: '1':ILK, '2':e-paygen, '3':Ƽ������ */
            BINFiller = new String[MAXSIZE];      /* ����                          */

    /* ���������� ��ȸ��� */
    public String[]
            SITransactionNo = new String[MAXSIZE],    // �ŷ���ȣ
            SIStatus = new String[MAXSIZE],    // ����:O, ����: X
            SIRespCode = new String[MAXSIZE],    // '0000' : ����ó��
            SIAgenMembDealSele = new String[MAXSIZE],    // ��ü���౸��
            SIStartSele = new String[MAXSIZE],    // ���ÿ���
            SIEntrNumb = new String[MAXSIZE],    // ����ڹ�ȣ
            SIShopName = new String[MAXSIZE],    // ������
            SIMembNumbGene = new String[MAXSIZE],    // �Ϲ� ��������ȣ
            SIMembNumbNoin = new String[MAXSIZE],    // ������ ��������ȣ
            SIAlloMontType = new String[MAXSIZE],    // �Һ�����
            SIFiller = new String[MAXSIZE];    // ����

    /* ������ü���ΰ�� */
    public String[]
            ACTransactionNo = new String[MAXSIZE],
            ACStatus = new String[MAXSIZE],
            ACTradeDate = new String[MAXSIZE],
            ACTradeTime = new String[MAXSIZE],
            ACAcctSele = new String[MAXSIZE],
            ACFeeSele = new String[MAXSIZE],
            ACInjaName = new String[MAXSIZE],
            ACPareBankCode = new String[MAXSIZE],
            ACPareAcctNo = new String[MAXSIZE],
            ACCustBankCode = new String[MAXSIZE],
            ACCustAcctNo = new String[MAXSIZE],
            ACAmount = new String[MAXSIZE],
            ACBankTransactionNo = new String[MAXSIZE],
            ACIpgumNm = new String[MAXSIZE],
            ACBankFee = new String[MAXSIZE],
            ACBankAmount = new String[MAXSIZE],
            ACBankRespCode = new String[MAXSIZE],
            ACMessage1 = new String[MAXSIZE],
            ACMessage2 = new String[MAXSIZE],
            ACEntrNumb = new String[MAXSIZE],
            ACShopPhone = new String[MAXSIZE],
            ACCavvSele = new String[MAXSIZE],
            ACFiller = new String[MAXSIZE],
            ACEncData = new String[MAXSIZE];

    public KSPayApprovalCancelBean(String IPAddr, int Port) {
        this.IPAddr = IPAddr;
        this.Port = Port;

        this.SendCount = 0;
        this.ReceiveCount = 0;
        this.SendMsg = "";
    }

    public KSPayApprovalCancelBean(String ServerName, String PathName) throws IOException {
        Properties Props = new Properties();
        String FileName = null;
        String tmp;

        try {
            FileName = PathName + "new_env.cfg";

            FileInputStream in = new FileInputStream(FileName);
            Props.load(in);
        } catch (IOException e) {
            throw new IOException("[KSPayApprovalCancelBean] new_env.cfg file not found!");
        }

        if ((this.IPAddr = Props.getProperty(ServerName + "_IPADDRESS")) == null)
            throw new IOException("[KSPayApprovalCancelBean] Exception on get property named 'IPADDRESS'");

        if ((tmp = Props.getProperty(ServerName + "_PORT")) != null)
            this.Port = Integer.parseInt(tmp);
        else
            throw new IOException("[KSPayApprovalCancelBean] Exception on get property named 'PORT'");

        this.SendCount = 0;
        this.ReceiveCount = 0;
        this.SendMsg = "";
    }

    public KSPayApprovalCancelBean(String ServerName) throws IOException {
        Properties Props = new Properties();
        String FileName = System.getProperty("IPGADMIN_PATH");
        String tmp;

        try {
            if (FileName == null) FileName = "/export/home/ipgadmin/cfg/new_env.cfg";

            FileInputStream in = new FileInputStream(FileName);
            Props.load(in);
        } catch (IOException e) {
            throw new IOException("[KSPayApprovalCancelBean] Exception on config file\nYou must execute java -D IPGADMIN_PATH=filenameWithAbsolutePath classfile");
        }

        if ((this.IPAddr = Props.getProperty(ServerName + "_IPADDRESS")) == null)
            throw new IOException("[KSPayApprovalCancelBean] Exception on get property named 'IPADDRESS'");

        if ((tmp = Props.getProperty(ServerName + "_PORT")) != null)
            this.Port = Integer.parseInt(tmp);
        else
            throw new IOException("[KSPayApprovalCancelBean] Exception on get property named 'PORT'");

        this.SendMsg = "";
        this.SendCount = 0;
        this.ReceiveCount = 0;
    }

    public boolean HeadMessage
            (
                    String pEncType,     // 0: ��ȭ����, 1:openssl, 2: seed
                    String pVersion,     // ��������
                    String pType,     // ����
                    String pResend,     // ���۱��� : 0 : ó��,  2: ������
                    String pRequestDate,     // ��û���� : yyyymmddhhmmss
                    String pStoreId,     // �������̵�
                    String pOrderNumber,     // �ֹ���ȣ
                    String pUserName,     // �ֹ��ڸ�
                    String pIdNum,     // �ֹι�ȣ or ����ڹ�ȣ
                    String pEmail,     // email
                    String pGoodType,     // ��ǰ���� 0 : �ǹ�, 1 : ������
                    String pGoodName,     // ��ǰ��
                    String pKeyInType,     // KeyInType ���� : S : Swap, K: KeyInType
                    String pLineType,     // lineType 0 : offline, 1:internet, 2:Mobile
                    String pPhoneNo,     // �޴�����ȣ
                    String pApprovalCount,     // ���ս��ΰ���
                    String pFiller)     // ����
    {
        StringBuffer TmpHeadMsg = new StringBuffer();

        TmpHeadMsg.append(this.format(pEncType, 1, 'X'));
        TmpHeadMsg.append(this.format(pVersion, 4, 'X'));
        TmpHeadMsg.append(this.format(pType, 2, 'X'));
        TmpHeadMsg.append(this.format(pResend, 1, 'X'));
        TmpHeadMsg.append(this.format(pRequestDate, 14, 'X'));
        TmpHeadMsg.append(this.format(pStoreId, 10, 'X'));
        TmpHeadMsg.append(this.format(pOrderNumber, 50, 'X'));
        TmpHeadMsg.append(this.format(pUserName, 50, 'X'));
        TmpHeadMsg.append(this.format(pIdNum, 13, 'X'));
        TmpHeadMsg.append(this.format(pEmail, 50, 'X'));
        TmpHeadMsg.append(this.format(pGoodType, 1, 'X'));
        TmpHeadMsg.append(this.format(pGoodName, 50, 'X'));
        TmpHeadMsg.append(this.format(pKeyInType, 1, 'X'));
        TmpHeadMsg.append(this.format(pLineType, 1, 'X'));
        TmpHeadMsg.append(this.format(pPhoneNo, 12, 'X'));
        TmpHeadMsg.append(this.format(pApprovalCount, 1, 'X'));
        TmpHeadMsg.append(this.format(pFiller, 35, 'X'));

        this.HeadMsg = TmpHeadMsg.toString();
        System.out.println("HeadMsg=[" + TmpHeadMsg.toString() + "]");

        return true;
    }

    // �ſ�ī����ο�û Body 1
    public boolean CreditDataMessage(
            String ApprovalType,    // ���α���
            String InterestType,    // �Ϲ�/�����ڱ��� 1:�Ϲ� 2:������
            String TrackII,    // ī���ȣ=��ȿ�Ⱓ  or �ŷ���ȣ
            String Installment,    // �Һ�  00�Ͻú�
            String Amount,    // �ݾ�
            String Passwd,    // ��й�ȣ ��2�ڸ�
            String IdNum,    // �ֹι�ȣ  ��7�ڸ�, ����ڹ�ȣ10
            String CurrencyType,    // ��ȭ���� 0:��ȭ 1: ��ȭ
            String BatchUseType,    // �ŷ���ȣ��ġ��뱸��  0:�̻�� 1:���
            String CardSendType,    // ī���������� 0:������ 1:ī���ȣ,��ȿ�Ⱓ,�Һ�,�ݾ�,��������ȣ 2:ī���ȣ��14�ڸ� + "XXXX",��ȿ�Ⱓ,�Һ�,�ݾ�,��������ȣ
            String VisaAuthYn,    // ������������ 0:������,7:SSL,9:��������
            String Domain,    // ������ ��ü������(PG��ü��)
            String IpAddr,    // IP ADDRESS ��ü������(PG��ü��)
            String BusinessNumber,    // ����� ��ȣ ��ü������(PG��ü��)
            String Filler,    // ����
            String AuthType,    // ISP : ISP�ŷ�, MP1, MP2 : MPI�ŷ�, SPACE : �Ϲݰŷ�
            String MPIPositionType,    // K : KSNET, R : Remote, C : ��3���, SPACE : �Ϲݰŷ�
            String MPIReUseType,    // Y :  ����, N : ����ƴ�
            String EncData)    // MPI, ISP ������
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(InterestType, 1, 'X'));
        TmpSendMsg.append(this.format(TrackII, 40, 'X'));
        TmpSendMsg.append(this.format(Installment, 2, '9'));
        TmpSendMsg.append(this.format(Amount, 9, '9'));
        TmpSendMsg.append(this.format(Passwd, 2, 'X'));
        TmpSendMsg.append(this.format(IdNum, 10, 'X'));
        TmpSendMsg.append(this.format(CurrencyType, 1, 'X'));
        TmpSendMsg.append(this.format(BatchUseType, 1, 'X'));
        TmpSendMsg.append(this.format(CardSendType, 1, 'X'));
        TmpSendMsg.append(this.format(VisaAuthYn, 1, 'X'));
        TmpSendMsg.append(this.format(Domain, 40, 'X'));
        TmpSendMsg.append(this.format(IpAddr, 20, 'X'));
        TmpSendMsg.append(this.format(BusinessNumber, 10, 'X'));
        TmpSendMsg.append(this.format(Filler, 135, 'X'));
        TmpSendMsg.append(this.format(AuthType, 1, 'X'));
        TmpSendMsg.append(this.format(MPIPositionType, 1, 'X'));
        TmpSendMsg.append(this.format(MPIReUseType, 1, 'X'));
        TmpSendMsg.append(EncData);

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("CreditDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    public boolean VirtualAccountDataMessage(
            String ApprovalType,    // ���α���
            String BankCode,    // �����ڵ�
            String Amount,    // �ݾ�
            String CloseDate,    // ��������
            String CloseTime,    // �����ð�
            String EscrowSele,    // ����ũ�����뱸��: 0:�������, 1:����, 2:��������
            String VirFixSele,    // ������¹�ȣ��������
            String VirAcctNo,    // ������¹�ȣ
            String OrgTransactionNo,    // ���ŷ��ŷ���ȣ
            String Filler)    // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(BankCode, 6, 'X'));
        TmpSendMsg.append(this.format(Amount, 9, '9'));
        TmpSendMsg.append(this.format(CloseDate, 8, 'X'));
        TmpSendMsg.append(this.format(CloseTime, 6, 'X'));
        TmpSendMsg.append(this.format(EscrowSele, 1, 'X'));
        TmpSendMsg.append(this.format(VirFixSele, 1, 'X'));
        TmpSendMsg.append(this.format(VirAcctNo, 15, 'X'));
        TmpSendMsg.append(this.format(OrgTransactionNo, 12, 'X'));
        TmpSendMsg.append(this.format(Filler, 52, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("VirtualAccountDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    public boolean VirtualAccountDataMessage(
            String ApprovalType,      // ���α���
            String BankCode,      // �����ڵ�
            String Amount,      // �ݾ�
            String Filler)      // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(BankCode, 6, 'X'));
        TmpSendMsg.append(this.format(Amount, 9, '9'));
        TmpSendMsg.append(this.format(Filler, 81, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("VirtualAccountDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    // �����н�ī��
    public boolean WorldPassDataMessage(
            String ApprovalType,      // ���α���
            String TrackII,      // ī���ȣ=��ȿ�Ⱓ  or �ŷ���ȣ
            String Passwd,      // ��й�ȣ ��2�ڸ�
            String Amount,      // �ݾ�
            String WorldPassType,      // ���ĺ�ī�屸��
            String AdultType,      // ����Ȯ�α���
            String CardSendType,      // ī���������� 0:������ 1:ī���ȣ,��ȿ�Ⱓ,�Һ�,�ݾ�,��������ȣ 2:ī���ȣ��14�ڸ� + "XXXX",��ȿ�Ⱓ,�Һ�,�ݾ�,��������ȣ
            String Filler)      // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(TrackII + "=4912", 40, 'X'));
        TmpSendMsg.append(this.format(Passwd, 4, 'X'));
        TmpSendMsg.append(this.format(Amount, 9, '9'));
        TmpSendMsg.append(this.format(WorldPassType, 1, 'X'));
        TmpSendMsg.append(this.format(AdultType, 1, 'X'));
        TmpSendMsg.append(this.format(CardSendType, 1, 'X'));
        TmpSendMsg.append(this.format(Filler, 40, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("WorldPassDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    // ����Ʈī�����
    public boolean PointDataMessage(
            String ApprovalType,       // ���α���
            String TrackII,       // ī���ȣ=��ȿ�Ⱓ  or �ŷ���ȣ
            String Amount,       // �ݾ�
            String Passwd,       // ��й�ȣ ��4�ڸ�
            String SaleType,       // �Ǹű���
            String Filler)       // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(TrackII + "=4912", 40, 'X'));
        TmpSendMsg.append(this.format(Amount, 9, '9'));
        TmpSendMsg.append(this.format(Passwd, 4, 'X'));
        TmpSendMsg.append(this.format(SaleType, 2, 'X'));
        TmpSendMsg.append(this.format(Filler, 40, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("PointDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    // ���ݿ������߱�
    public boolean CashBillDataMessage(
            String ApprovalType,//H000:�Ϲݹ߱�, H200:������ü, H600:�������
            String TransactionNo,//�ԱݿϷ�� ������ü, ������� �ŷ���ȣ
            String IssuSele,//0:�Ϲݹ߱�(PG���ŷ���ȣ �ߺ�üũ), 1:�ܵ��߱�(�ֹ���ȣ �ߺ�üũ : PG���ŷ� ����), 2:�����߱�(�ߺ�üũ ����)
            String UserInfoSele,//0:�ֹε�Ϲ�ȣ, 1:����ڹ�ȣ, 2:ī���ȣ, 3:�޴�����ȣ, 4:��Ÿ
            String UserInfo,//�ֹε�Ϲ�ȣ, ����ڹ�ȣ, ī���ȣ, �޴�����ȣ, ��Ÿ
            String TranSele,//0: ����, 1: �����
            String CallCode,//��ȭ�ڵ�  (0: ��ȭ, 1: ��ȭ)
            String SupplyAmt,//���ް���
            String TaxAmt,//����
            String SvcAmt,//�����
            String TotAmt,//���ݿ����� �߱ޱݾ�
            String Filler)        //����
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(TransactionNo, 12, 'X'));
        TmpSendMsg.append(this.format(IssuSele, 1, 'X'));
        TmpSendMsg.append(this.format(UserInfoSele, 1, 'X'));
        TmpSendMsg.append(this.format(UserInfo, 37, 'X'));
        TmpSendMsg.append(this.format(TranSele, 1, 'X'));
        TmpSendMsg.append(this.format(CallCode, 1, 'X'));
        TmpSendMsg.append(this.format(SupplyAmt, 9, '9'));
        TmpSendMsg.append(this.format(TaxAmt, 9, '9'));
        TmpSendMsg.append(this.format(SvcAmt, 9, '9'));
        TmpSendMsg.append(this.format(TotAmt, 9, '9'));
        TmpSendMsg.append(this.format(Filler, 147, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("CashBillDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //�ڵ��� ����1��
    public boolean Mobile1DataMessage(
            String ApprovalType,  /* �����ڵ�                     */
            String MobileNo,  /* �޴�����ȣ                   */
            String SocialNo,  /* ������ �ֹι�ȣ              */
            String UsersocialNo,  /* LGT �ǻ���� �����ÿ��� ��� */
            String Commsele,  /* SKT, KTF, LGT                */
            String Filler)  /* ����                         */ {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(MobileNo, 12, 'X'));
        TmpSendMsg.append(this.format(SocialNo, 13, 'X'));
        TmpSendMsg.append(this.format(UsersocialNo, 13, 'X'));
        TmpSendMsg.append(this.format(Commsele, 3, 'X'));
        TmpSendMsg.append(this.format(Filler, 155, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("Mobile1DataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //�ڵ��� ����2��
    public boolean Mobile2DataMessage(
            String ApprovalType,  /* �����ڵ�     */
            String TransactionNo,  /* �ŷ���ȣ     */
            String Smsval,  /* sms �����ڵ� */
            String Filler) {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(TransactionNo, 12, 'X'));
        TmpSendMsg.append(this.format(Smsval, 20, 'X'));
        TmpSendMsg.append(this.format(Filler, 164, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("Mobile2DataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //�ڵ��� ����1��
    public boolean MobileAppr1DataMessage(
            String ApprovalType,   /* �����ڵ�                     */
            String MobileNo,   /* �޴�����ȣ                   */
            String SocialNo,   /* ������ �ֹι�ȣ              */
            String UsersocialNo,   /* LGT �ǻ���� �����ÿ��� ��� */
            String Commsele,   /* SKT, KTF, LGT                */
            String ApprAmount,   /* ���αݾ�                     */
            String BypassMsg,   /* Echo Msg                     */
            String CompSele,   /* ����ڵ�                     */
            String Filler)   /* ����                         */ {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(MobileNo, 12, 'X'));
        TmpSendMsg.append(this.format(SocialNo, 13, 'X'));
        TmpSendMsg.append(this.format(UsersocialNo, 13, 'X'));
        TmpSendMsg.append(this.format(Commsele, 3, 'X'));
        TmpSendMsg.append(this.format(ApprAmount, 9, '9'));
        TmpSendMsg.append(this.format(BypassMsg, 100, 'X'));
        TmpSendMsg.append(this.format(CompSele, 1, 'X'));
        TmpSendMsg.append(this.format(Filler, 145, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("MobileAppr1DataMessage=[" + TmpSendMsg.toString() + "]");
        return true;
    }

    //�ڵ��� ����2��
    public boolean MobileAppr2DataMessage(
            String ApprovalType,  /* �����ڵ�     */
            String TransactionNo,  /* �ŷ���ȣ     */
            String Smsval,  /* sms �����ڵ� */
            String BypassMsg,  /* BypassMsg    */
            String Filler) {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(TransactionNo, 12, 'X'));
        TmpSendMsg.append(this.format(Smsval, 20, 'X'));
        TmpSendMsg.append(this.format(BypassMsg, 20, 'X'));
        TmpSendMsg.append(this.format(Filler, 164, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("Mobile2DataMessage=[" + TmpSendMsg.toString() + "]");
        return true;
    }

    //������ü���ۿ�û������ �����.(send)
    public boolean AcctRequest_send(
            String ApprovalType,        // ���α���
            String AcctSele,        // ������ü��������
            String FeeSele,        // ��/�ĺ�������
            String PareBankCode,        // ����������ڵ�
            String PareAcctNo,        // ����¹�ȣ
            String CustBankCode,        // �����������ڵ�
            String Amount,        // �ݾ�
            String InjaName,        // ���ڸ�(������)
            String Filler)        // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(AcctSele, 1, 'X'));
        TmpSendMsg.append(this.format(FeeSele, 1, 'X'));
        TmpSendMsg.append(this.format(PareBankCode, 6, 'X'));
        TmpSendMsg.append(this.format(PareAcctNo, 15, 'X'));
        TmpSendMsg.append(this.format(CustBankCode, 6, 'X'));
        TmpSendMsg.append(this.format(Amount, 13, '9'));
        TmpSendMsg.append(this.format(InjaName, 16, 'X'));
        TmpSendMsg.append(this.format(Filler, 38, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("AcctRequest_send=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //������ü �������� ��û������ �����.
    public boolean AcctRequest_iappr(
            String ApprovalType,  // ���α��� �ڵ�
            String AcctSele,  // ������ü ���� - 1:Dacom, 2:Pop Banking,	3:Scrapping ������ü, 4:������������ü, 5:�ݰ��������ü
            String FeeSele,  // ������ü ���� - ��/�ĺ������� -	1:����,	2:�ĺ�
            String TransactionNo,  // �ŷ���ȣ
            String BankCode,  // �Աݸ�����ڵ�
            String Amount,  // �ݾ�	(�������ݾ�)
            String CustBankInja,  // ��ݸ�����ڵ�
            String BankTransactionNo,  // ����ŷ���ȣ
            String Filler,  //
            String CertData)  // ��������
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(AcctSele, 1, 'X'));
        TmpSendMsg.append(this.format(FeeSele, 1, 'X'));
        TmpSendMsg.append(this.format(TransactionNo, 12, 'X'));
        TmpSendMsg.append(this.format(BankCode, 6, 'X'));
        TmpSendMsg.append(this.format(Amount, 13, '9'));
        TmpSendMsg.append(this.format(CustBankInja, 30, 'X'));
        TmpSendMsg.append(this.format(BankTransactionNo, 30, 'X'));
        TmpSendMsg.append(this.format(Filler, 53, 'X'));
        TmpSendMsg.append(CertData);

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("AcctRequest_iappr=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //ī�� ���
    public boolean CancelDataMessage(
            String ApprovalType,         // ���α���
            String CancelType,         // ���ó������ 1:�ŷ���ȣ, 2:�ֹ���ȣ
            String TransactionNo,         // �ŷ���ȣ
            String TradeDate,         // �ŷ�����
            String OrderNumber,         // �ֹ���ȣ
            String CancelData,         // ���data(�����߰�)
            String Refundcheck,         // ���ݿ����� ��ҿ��� (1.�ŷ����, 2.�����߱����, 3.��Ÿ)
            String Filler)         // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(CancelType, 1, 'X'));
        TmpSendMsg.append(this.format(TransactionNo, 12, 'X'));
        TmpSendMsg.append(this.format(TradeDate, 8, 'X'));
        TmpSendMsg.append(this.format(OrderNumber, 50, 'X'));
        TmpSendMsg.append(this.format(CancelData, 42, 'X'));
        TmpSendMsg.append(this.format(Refundcheck, 1, 'X'));
        TmpSendMsg.append(this.format(Filler, 32, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("CancelDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //ī�� BIN check
    public boolean CardBinDataMessage(
            String ApprovalType,       // ���α���
            String TrackII,       // ī���ȣ
            String Filler)       // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(TrackII, 40, 'X'));
        TmpSendMsg.append(this.format(Filler, 56, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("CardBinDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    // �������� �󼼿�û(A700)
    public boolean ShopInfoDetailDataMessage(
            String ApprovalType,       // ���α���
            String ShopId,       // �������̵�
            String BusiSele,       // ��������
            String CardCode,       // ī���ڵ�
            String Filler)       // ��Ÿ
    {
        StringBuffer TmpSendMsg = new StringBuffer();

        TmpSendMsg.append(this.format(ApprovalType, 4, 'X'));
        TmpSendMsg.append(this.format(ShopId, 10, 'X'));
        TmpSendMsg.append(this.format(BusiSele, 1, 'X'));
        TmpSendMsg.append(this.format(CardCode, 6, 'X'));
        TmpSendMsg.append(this.format(Filler, 79, 'X'));

        this.SendMsg += TmpSendMsg.toString();
        this.SendCount++;
        System.out.println("ShopInfoDetailDataMessage=[" + TmpSendMsg.toString() + "]");

        return true;
    }

    //�������Ŀ� ������� ������ �´�.

    public boolean ReceiveMessage() throws IOException {
        this.ReceiveMsg = "";
        StringBuffer TmpReceiveMsg = new StringBuffer();

        String Len = new String(this.KSPaySocket.read(4), MSG_ENCODING);          // ������ ����
        if (Len == null || Len.trim().equals("")) return false;
        this.EncType = new String(this.KSPaySocket.read(1), MSG_ENCODING);          // 0: ��ȭ����, 1:openssl, 2: seed
        this.Version = new String(this.KSPaySocket.read(4), MSG_ENCODING);          // ��������
        this.Type = new String(this.KSPaySocket.read(2), MSG_ENCODING);          // ����
        this.Resend = new String(this.KSPaySocket.read(1), MSG_ENCODING);          // ���۱��� : 0 : ó��,  2: ������
        this.RequestDate = new String(this.KSPaySocket.read(14), MSG_ENCODING);          // ��û���� : yyyymmddhhmmss
        this.StoreId = new String(this.KSPaySocket.read(10), MSG_ENCODING);          // �������̵�
        this.OrderNumber = new String(this.KSPaySocket.read(50), MSG_ENCODING);          // �ֹ���ȣ
        this.UserName = new String(this.KSPaySocket.read(50), MSG_ENCODING);          // �ֹ��ڸ�
        this.IdNum = new String(this.KSPaySocket.read(13), MSG_ENCODING);          // �ֹι�ȣ or ����ڹ�ȣ
        this.Email = new String(this.KSPaySocket.read(50), MSG_ENCODING);          // email
        this.GoodType = new String(this.KSPaySocket.read(1), MSG_ENCODING);          // ��ǰ���� 0 : �ǹ�, 1 : ������
        this.GoodName = new String(this.KSPaySocket.read(50), MSG_ENCODING);          // ��ǰ��
        this.KeyInType = new String(this.KSPaySocket.read(1), MSG_ENCODING);          // KeyInType ���� : 1 : Swap, 2: KeyIn
        this.LineType = new String(this.KSPaySocket.read(1), MSG_ENCODING);          // lineType 0 : offline, 1:internet, 2:Mobile
        this.PhoneNo = new String(this.KSPaySocket.read(12), MSG_ENCODING);          // �޴�����ȣ
        this.ApprovalCount = new String(this.KSPaySocket.read(1), MSG_ENCODING);          // ���ΰ���
        this.HeadFiller = new String(this.KSPaySocket.read(35), MSG_ENCODING);          // ����

        TmpReceiveMsg.append(Len);
        TmpReceiveMsg.append(this.EncType);
        TmpReceiveMsg.append(this.Version);
        TmpReceiveMsg.append(this.Type);
        TmpReceiveMsg.append(this.Resend);
        TmpReceiveMsg.append(this.RequestDate);
        TmpReceiveMsg.append(this.StoreId);
        TmpReceiveMsg.append(this.OrderNumber);
        TmpReceiveMsg.append(this.UserName);
        TmpReceiveMsg.append(this.IdNum);
        TmpReceiveMsg.append(this.Email);
        TmpReceiveMsg.append(this.GoodType);
        TmpReceiveMsg.append(this.GoodName);
        TmpReceiveMsg.append(this.KeyInType);
        TmpReceiveMsg.append(this.LineType);
        TmpReceiveMsg.append(this.PhoneNo);
        TmpReceiveMsg.append(this.ApprovalCount);
        TmpReceiveMsg.append(this.HeadFiller);

        this.ReceiveMsg = TmpReceiveMsg.toString();
        System.out.println("Header ReceiveMsg=[" + TmpReceiveMsg.toString() + "]");
        System.out.println("ReceiveCount=[" + this.ReceiveCount + "]\n");
        this.ReceiveCount = Integer.parseInt(this.ApprovalCount);

        return ReceiveDataMessage(Integer.parseInt(this.ApprovalCount));
    }

    public boolean ReceiveDataMessage(int iCnt) throws IOException {
        int iCreidtCnt = 0;
        int iVirAcctCnt = 0;
        int iPhoneCnt = 0;

        StringBuffer TmpReceiveMsg = new StringBuffer();

        //System.out.println("Header ApprovalCount=["+iCnt+"]");
        for (int i = 0; i < iCnt; i++) {
            this.ApprovalType[i] = new String(KSPaySocket.read(4), MSG_ENCODING); // ���α���
            System.out.println("ApprovalType=[" + this.ApprovalType[i] + "]");

            // �ſ�ī��
            if (this.ApprovalType[i].substring(0, 1).equals("1") || this.ApprovalType[i].substring(0, 1).equals("I")) {
                if (this.ApprovalType[i].substring(1, 2).equals("5")) {
                    this.TransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);    // �ŷ���ȣ
                    this.Status[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);    // ���� O : ����, X : ����
                    this.TradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);    // �ŷ�����
                    this.TradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);    // �ŷ��ð�
                    this.IssCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);    // �߱޻��ڵ�
                    this.Message1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);    // �޽���1
                    this.Message2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);    // �޽���2

                    TmpReceiveMsg = new StringBuffer();
                    TmpReceiveMsg.append(this.ApprovalType[i]);
                    TmpReceiveMsg.append(this.TransactionNo[i]);
                    TmpReceiveMsg.append(this.Status[i]);
                    TmpReceiveMsg.append(this.TradeDate[i]);
                    TmpReceiveMsg.append(this.TradeTime[i]);
                    TmpReceiveMsg.append(this.IssCode[i]);
                    TmpReceiveMsg.append(this.Message1[i]);
                    TmpReceiveMsg.append(this.Message2[i]);
                } else {
                    this.TransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // �ŷ���ȣ
                    this.Status[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ���� O : ����, X : ����
                    this.TradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);   // �ŷ�����
                    this.TradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �ŷ��ð�
                    this.IssCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �߱޻��ڵ�
                    this.AquCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // ���Ի��ڵ�
                    this.AuthNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // ���ι�ȣ or ������ �����ڵ�
                    this.Message1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // �޽���1
                    this.Message2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // �޽���2
                    this.CardNo[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ī���ȣ
                    this.ExpDate[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);   // ��ȿ�Ⱓ
                    this.Installment[i] = new String(this.KSPaySocket.read(2), MSG_ENCODING);   // �Һ�
                    this.Amount[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);   // �ݾ�
                    this.MerchantNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);   // ��������ȣ
                    this.AuthSendType[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ���۱���= new String(this.read(2));
                    this.ApprovalSendType[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ���۱���(0 : ����, 1 : ����, 2: ��ī��)
                    this.Point1[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // Point1
                    this.Point2[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // Point2
                    this.Point3[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // Point3
                    this.Point4[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // Point4
                    this.VanTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // Point4
                    this.Filler[i] = new String(this.KSPaySocket.read(82), MSG_ENCODING);   // ����
                    this.AuthType[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // I : ISP�ŷ�, M : MPI�ŷ�, SPACE : �Ϲݰŷ�
                    this.MPIPositionType[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // K : KSNET, R : Remote, C : ��3���, SPACE : �Ϲݰŷ�
                    this.MPIReUseType[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // Y : ����, N : ����ƴ�

                    String EncLen = "";
                    if (AuthType[i] == "" || AuthType[i].trim().equals("")) {
                        this.EncData[i] = "";
                    } else {
                        EncLen = new String(this.KSPaySocket.read(5), MSG_ENCODING);
                        this.EncData[i] = new String(this.KSPaySocket.read(Integer.parseInt(EncLen)), MSG_ENCODING); // MPI, ISP ������
                    }

                    TmpReceiveMsg = new StringBuffer();
                    TmpReceiveMsg.append(this.ApprovalType[i]);
                    TmpReceiveMsg.append(this.TransactionNo[i]);
                    TmpReceiveMsg.append(this.Status[i]);
                    TmpReceiveMsg.append(this.TradeDate[i]);
                    TmpReceiveMsg.append(this.TradeTime[i]);
                    TmpReceiveMsg.append(this.IssCode[i]);
                    TmpReceiveMsg.append(this.AquCode[i]);
                    TmpReceiveMsg.append(this.AuthNo[i]);
                    TmpReceiveMsg.append(this.Message1[i]);
                    TmpReceiveMsg.append(this.Message2[i]);
                    TmpReceiveMsg.append(this.CardNo[i]);
                    TmpReceiveMsg.append(this.ExpDate[i]);
                    TmpReceiveMsg.append(this.Installment[i]);
                    TmpReceiveMsg.append(this.Amount[i]);
                    TmpReceiveMsg.append(this.MerchantNo[i]);
                    TmpReceiveMsg.append(this.AuthSendType[i]);
                    TmpReceiveMsg.append(this.ApprovalSendType[i]);
                    TmpReceiveMsg.append(this.Point1[i]);
                    TmpReceiveMsg.append(this.Point2[i]);
                    TmpReceiveMsg.append(this.Point3[i]);
                    TmpReceiveMsg.append(this.Point4[i]);
                    TmpReceiveMsg.append(this.VanTransactionNo[i]);
                    TmpReceiveMsg.append(this.Filler[i]);
                    TmpReceiveMsg.append(this.AuthType[i]);
                    TmpReceiveMsg.append(this.MPIPositionType[i]);
                    TmpReceiveMsg.append(this.MPIReUseType[i]);
                    TmpReceiveMsg.append(EncLen);
                    TmpReceiveMsg.append(this.EncData[i]);
                }
                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Credit ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // ����Ʈī��
            else if (this.ApprovalType[i].substring(0, 1).equals("4")) {
                this.PTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);      // �ŷ���ȣ
                this.PStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);      // ���� O : ���� , X : ����
                this.PTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);      // �ŷ�����
                this.PTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);      // �ŷ��ð�
                this.PIssCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);      // �߱޻��ڵ�
                this.PAuthNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);      // ���ι�ȣ or ������ �����ڵ�
                this.PMessage1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);      // �޽���1
                this.PMessage2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);      // �޽���2
                this.PPoint1[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);      // �ŷ�����Ʈ
                this.PPoint2[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);      // ��������Ʈ
                this.PPoint3[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);      // ��������Ʈ
                this.PPoint4[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);      // ����������Ʈ
                this.PMerchantNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);      // ��������ȣ
                this.PNotice1[i] = new String(this.KSPaySocket.read(40), MSG_ENCODING);      //
                this.PNotice2[i] = new String(this.KSPaySocket.read(40), MSG_ENCODING);      //
                this.PNotice3[i] = new String(this.KSPaySocket.read(40), MSG_ENCODING);      //
                this.PNotice4[i] = new String(this.KSPaySocket.read(40), MSG_ENCODING);      //
                this.PFiller[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);      // ����

                TmpReceiveMsg = new StringBuffer();
                TmpReceiveMsg.append(this.ApprovalType[i]);
                TmpReceiveMsg.append(this.PTransactionNo[i]);
                TmpReceiveMsg.append(this.PStatus[i]);
                TmpReceiveMsg.append(this.PTradeDate[i]);
                TmpReceiveMsg.append(this.PTradeTime[i]);
                TmpReceiveMsg.append(this.PIssCode[i]);
                TmpReceiveMsg.append(this.PAuthNo[i]);
                TmpReceiveMsg.append(this.PMessage1[i]);
                TmpReceiveMsg.append(this.PMessage2[i]);
                TmpReceiveMsg.append(this.PPoint1[i]);
                TmpReceiveMsg.append(this.PPoint2[i]);
                TmpReceiveMsg.append(this.PPoint3[i]);
                TmpReceiveMsg.append(this.PPoint4[i]);
                TmpReceiveMsg.append(this.PMerchantNo[i]);
                TmpReceiveMsg.append(this.PNotice1[i]);
                TmpReceiveMsg.append(this.PNotice2[i]);
                TmpReceiveMsg.append(this.PNotice3[i]);
                TmpReceiveMsg.append(this.PNotice4[i]);
                TmpReceiveMsg.append(this.PFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Point ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // �������
            else if (this.ApprovalType[i].substring(0, 1).equals("6")) {
                this.VATransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);
                this.VAStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);
                this.VATradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);
                this.VATradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);
                this.VABankCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);
                this.VAVirAcctNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);
                this.VAName[i] = new String(this.KSPaySocket.read(30), MSG_ENCODING);
                this.VACloseDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);
                this.VACloseTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);
                this.VARespCode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);
                this.VAMessage1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);
                this.VAMessage2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);
                this.VAFiller[i] = new String(this.KSPaySocket.read(36), MSG_ENCODING);

                TmpReceiveMsg = new StringBuffer();
                TmpReceiveMsg.append(this.ApprovalType[i]);
                TmpReceiveMsg.append(this.VATransactionNo[i]);
                TmpReceiveMsg.append(this.VAStatus[i]);
                TmpReceiveMsg.append(this.VATradeDate[i]);
                TmpReceiveMsg.append(this.VATradeTime[i]);
                TmpReceiveMsg.append(this.VABankCode[i]);
                TmpReceiveMsg.append(this.VAVirAcctNo[i]);
                TmpReceiveMsg.append(this.VAName[i]);
                TmpReceiveMsg.append(this.VACloseDate[i]);
                TmpReceiveMsg.append(this.VACloseTime[i]);
                TmpReceiveMsg.append(this.VARespCode[i]);
                TmpReceiveMsg.append(this.VAMessage1[i]);
                TmpReceiveMsg.append(this.VAMessage2[i]);
                TmpReceiveMsg.append(this.VAFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Virtual ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // �����н�
            else if (this.ApprovalType[i].substring(0, 1).equals("7")) {
                this.WPTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);
                this.WPStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);
                this.WPTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);
                this.WPTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);
                this.WPIssCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);
                this.WPAuthNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);
                this.WPBalanceAmount[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);
                this.WPLimitAmount[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);
                this.WPMessage1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);
                this.WPMessage2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);
                this.WPCardNo[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);
                this.WPAmount[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);
                this.WPMerchantNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);
                this.WPFiller[i] = new String(this.KSPaySocket.read(11), MSG_ENCODING);

                TmpReceiveMsg = new StringBuffer();
                TmpReceiveMsg.append(this.ApprovalType[i]);
                TmpReceiveMsg.append(this.WPTransactionNo[i]);
                TmpReceiveMsg.append(this.WPStatus[i]);
                TmpReceiveMsg.append(this.WPTradeDate[i]);
                TmpReceiveMsg.append(this.WPTradeTime[i]);
                TmpReceiveMsg.append(this.WPIssCode[i]);
                TmpReceiveMsg.append(this.WPAuthNo[i]);
                TmpReceiveMsg.append(this.WPBalanceAmount[i]);
                TmpReceiveMsg.append(this.WPLimitAmount[i]);
                TmpReceiveMsg.append(this.WPMessage1[i]);
                TmpReceiveMsg.append(this.WPMessage2[i]);
                TmpReceiveMsg.append(this.WPCardNo[i]);
                TmpReceiveMsg.append(this.WPAmount[i]);
                TmpReceiveMsg.append(this.WPMerchantNo[i]);
                TmpReceiveMsg.append(this.WPFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("WorldPass ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // ���ݿ�����
            else if (this.ApprovalType[i].substring(0, 1).equals("H")) {

                this.HTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // �ŷ���ȣ
                this.HStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // �������� O:���� X:����
                this.HCashTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // ���ݿ����� �ŷ���ȣ
                this.HIncomeType[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // 0: �ҵ�      1: ��ҵ�
                this.HTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);   // �ŷ� ���� ����
                this.HTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �ŷ� ���� �ð�
                this.HMessage1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���� message1
                this.HMessage2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���� message2
                this.HCashMessage1[i] = new String(this.KSPaySocket.read(20), MSG_ENCODING);   // ����û �޽��� 1
                this.HCashMessage2[i] = new String(this.KSPaySocket.read(20), MSG_ENCODING);   // ����û �޽��� 2
                this.HFiller[i] = new String(this.KSPaySocket.read(150), MSG_ENCODING);   // ����

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.HTransactionNo[i]);
                TmpReceiveMsg.append(this.HStatus[i]);
                TmpReceiveMsg.append(this.HCashTransactionNo[i]);
                TmpReceiveMsg.append(this.HIncomeType[i]);
                TmpReceiveMsg.append(this.HTradeDate[i]);
                TmpReceiveMsg.append(this.HTradeTime[i]);
                TmpReceiveMsg.append(this.HMessage1[i]);
                TmpReceiveMsg.append(this.HMessage2[i]);
                TmpReceiveMsg.append(this.HCashMessage1[i]);
                TmpReceiveMsg.append(this.HCashMessage2[i]);
                TmpReceiveMsg.append(this.HFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("CashBill ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // �ڵ��� ���� 1�� : M020
            else if (this.ApprovalType[i].substring(0, 3).equals("M02")) {

                this.MB1TransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   /* �ŷ���ȣ */
                this.MB1Status[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   /* ���� : O, X */
                this.MB1TradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);   /* �ŷ����� */
                this.MB1TradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   /* �ŷ��ð� */
                this.MB1Serverinfo[i] = new String(this.KSPaySocket.read(128), MSG_ENCODING);   /* ����INFO : ��ü������ ���� �ʿ� ���� ����� */
                this.MB1Smsval[i] = new String(this.KSPaySocket.read(20), MSG_ENCODING);   /* �ٳ��� ��� space */
                this.MB1Stanrespcode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);   /* �����ڵ� */
                this.MB1Message[i] = new String(this.KSPaySocket.read(200), MSG_ENCODING);   /* �����޽��� */
                this.MB1Filler[i] = new String(this.KSPaySocket.read(117), MSG_ENCODING);

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.MB1TransactionNo[i]);
                TmpReceiveMsg.append(this.MB1Status[i]);
                TmpReceiveMsg.append(this.MB1TradeDate[i]);
                TmpReceiveMsg.append(this.MB1TradeTime[i]);
                TmpReceiveMsg.append(this.MB1Serverinfo[i]);
                TmpReceiveMsg.append(this.MB1Smsval[i]);
                TmpReceiveMsg.append(this.MB1Stanrespcode[i]);
                TmpReceiveMsg.append(this.MB1Message[i]);
                TmpReceiveMsg.append(this.MB1Filler[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Mobile1 ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // �ڵ��� ���� 2�� : M030
            else if (this.ApprovalType[i].substring(0, 3).equals("M03")) {

                this.MB2TransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   /* �ŷ���ȣ */
                this.MB2Status[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   /* ����     */
                this.MB2TradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);   /* �ŷ����� */
                this.MB2TradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   /* �ŷ��ð� */
                this.MB2Stanrespcode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);   /* �����ڵ� */
                this.MB2Message[i] = new String(this.KSPaySocket.read(200), MSG_ENCODING);   /* ����޽��� */
                this.MB2Filler[i] = new String(this.KSPaySocket.read(115), MSG_ENCODING);   /* ���� */

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.MB2TransactionNo[i]);
                TmpReceiveMsg.append(this.MB2Status[i]);
                TmpReceiveMsg.append(this.MB2TradeDate[i]);
                TmpReceiveMsg.append(this.MB2TradeTime[i]);
                TmpReceiveMsg.append(this.MB2Stanrespcode[i]);
                TmpReceiveMsg.append(this.MB2Message[i]);
                TmpReceiveMsg.append(this.MB2Filler[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Mobile2 ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // �ڵ��� ���� 1�� : M120
            else if (this.ApprovalType[i].substring(0, 3).equals("M12") ||    //�ڵ�������1��
                    this.ApprovalType[i].substring(0, 3).equals("M11"))    //�ڵ����������
            {

                this.MTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);     /* �ŷ���ȣ */
                this.MStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);     /* ���� : O, X */
                this.MTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);     /* �ŷ����� */
                this.MTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);     /* �ŷ��ð� */
                this.MBalAmount[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);     /* �ܾ� */
                this.MRespCode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);     /* �����ڵ� */
                this.MRespMsg[i] = new String(this.KSPaySocket.read(200), MSG_ENCODING);     /* ����޽��� */
                this.MBypassMsg[i] = new String(this.KSPaySocket.read(100), MSG_ENCODING);     /* Echo �޽��� */
                this.MCompCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);     /* ����ڵ� */
                this.MFiller[i] = new String(this.KSPaySocket.read(150), MSG_ENCODING);     /* ���� */

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.MTransactionNo[i]);
                TmpReceiveMsg.append(this.MStatus[i]);
                TmpReceiveMsg.append(this.MTradeDate[i]);
                TmpReceiveMsg.append(this.MTradeTime[i]);
                TmpReceiveMsg.append(this.MBalAmount[i]);
                TmpReceiveMsg.append(this.MRespCode[i]);
                TmpReceiveMsg.append(this.MRespMsg[i]);
                TmpReceiveMsg.append(this.MBypassMsg[i]);
                TmpReceiveMsg.append(this.MCompCode[i]);
                TmpReceiveMsg.append(this.MFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Mobile ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // �ڵ��� ���� 2�� : M130
            else if (this.ApprovalType[i].substring(0, 3).equals("M13")) {

                this.MTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);      /* �ŷ���ȣ */
                this.MStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);      /* ���� : O, X */
                this.MTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);      /* �ŷ����� */
                this.MTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);      /* �ŷ��ð� */
                this.MBalAmount[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);      /* �ܾ� */
                this.MTid[i] = new String(this.KSPaySocket.read(20), MSG_ENCODING);      /* Tid */
                this.MRespCode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);      /* �����ڵ� */
                this.MRespMsg[i] = new String(this.KSPaySocket.read(200), MSG_ENCODING);      /* ����޽��� */
                this.MBypassMsg[i] = new String(this.KSPaySocket.read(100), MSG_ENCODING);      /* Echo �޽��� */
                this.MCompCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);      /* ����ڵ� */
                this.MCommSele[i] = new String(this.KSPaySocket.read(3), MSG_ENCODING);      /* SKT,KTF,LGT */
                this.MMobileNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);      /* �޴�����ȣ */
                this.MApprAmt[i] = new String(this.KSPaySocket.read(9), MSG_ENCODING);      /* ���αݾ� */
                this.MFiller[i] = new String(this.KSPaySocket.read(106), MSG_ENCODING);      /* ���� */

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.MTransactionNo[i]);
                TmpReceiveMsg.append(this.MStatus[i]);
                TmpReceiveMsg.append(this.MTradeDate[i]);
                TmpReceiveMsg.append(this.MTradeTime[i]);
                TmpReceiveMsg.append(this.MBalAmount[i]);
                TmpReceiveMsg.append(this.MTid[i]);
                TmpReceiveMsg.append(this.MRespCode[i]);
                TmpReceiveMsg.append(this.MRespMsg[i]);
                TmpReceiveMsg.append(this.MBypassMsg[i]);
                TmpReceiveMsg.append(this.MCompCode[i]);
                TmpReceiveMsg.append(this.MFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("Mobile ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // ������ü���ۿ�û
            else if (this.ApprovalType[i].substring(0, 3).equals("210") || this.ApprovalType[i].substring(0, 3).equals("240")) {
                this.ACTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // �ŷ���ȣ
                this.ACStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ��������:- O:���� X:����
                this.ACTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);   // �ŷ� ���� ����(YYYYMMDD)
                this.ACTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �ŷ� ���� �ð�(HHMMSS)
                this.ACAcctSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ������ü ���� -	1:Dacom, 2:Pop Banking,	3:�ǽð�������ü, 4:X
                this.ACFeeSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ��/�ĺ������� -	1:����,	2:�ĺ�
                this.ACPareBankCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �Աݸ���������ڵ�
                this.ACPareAcctNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);   // �Աݸ���� ��ȣ
                this.ACCustBankCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // ��������ڵ�
                this.ACAmount[i] = new String(this.KSPaySocket.read(13), MSG_ENCODING);   // �ݾ�
                this.ACInjaName[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���ڸ�(������)
                this.ACMessage1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���� message1
                this.ACMessage2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���� message2
                this.ACEntrNumb[i] = new String(this.KSPaySocket.read(10), MSG_ENCODING);   // ����ڹ�ȣ
                this.ACShopPhone[i] = new String(this.KSPaySocket.read(20), MSG_ENCODING);   // ��ȭ��ȣ
                this.ACFiller[i] = new String(this.KSPaySocket.read(49), MSG_ENCODING);   // ����

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.ACTransactionNo[i]);
                TmpReceiveMsg.append(this.ACStatus[i]);
                TmpReceiveMsg.append(this.ACTradeDate[i]);
                TmpReceiveMsg.append(this.ACTradeTime[i]);
                TmpReceiveMsg.append(this.ACAcctSele[i]);
                TmpReceiveMsg.append(this.ACFeeSele[i]);
                TmpReceiveMsg.append(this.ACPareBankCode[i]);
                TmpReceiveMsg.append(this.ACPareAcctNo[i]);
                TmpReceiveMsg.append(this.ACCustBankCode[i]);
                TmpReceiveMsg.append(this.ACAmount[i]);
                TmpReceiveMsg.append(this.ACInjaName[i]);
                TmpReceiveMsg.append(this.ACMessage1[i]);
                TmpReceiveMsg.append(this.ACMessage2[i]);
                TmpReceiveMsg.append(this.ACEntrNumb[i]);
                TmpReceiveMsg.append(this.ACShopPhone[i]);
                TmpReceiveMsg.append(this.ACFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("AcctRequest_send(2100,2400) ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // ������ü����ݿ���û || ������ü���ο�û || ������ü��ҿ�û
            else if (this.ApprovalType[i].substring(0, 1).equals("2")) {
                this.ACTransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // �ŷ���ȣ
                this.ACStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // �������� :���� X:����
                this.ACTradeDate[i] = new String(this.KSPaySocket.read(8), MSG_ENCODING);   // �ŷ� ���� ����(YYYYMMDD)
                this.ACTradeTime[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �ŷ� ���� �ð�(HHMMSS)
                this.ACAcctSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ������ü ���� -	1:Dacom, 2:Pop Banking,	3:�ǽð�������ü 4: ������������ü
                this.ACFeeSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ��/�ĺ������� -	1:����,	2:�ĺ�
                this.ACInjaName[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���ڸ�(�����μ�޼���-������)
                this.ACPareBankCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // �Աݸ�����ڵ�
                this.ACPareAcctNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);   // �Աݸ���¹�ȣ
                this.ACCustBankCode[i] = new String(this.KSPaySocket.read(6), MSG_ENCODING);   // ��ݸ�����ڵ�
                this.ACCustAcctNo[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);   // ��ݸ���¹�ȣ
                this.ACAmount[i] = new String(this.KSPaySocket.read(13), MSG_ENCODING);   // �ݾ�	(�������ݾ�)
                this.ACBankTransactionNo[i] = new String(this.KSPaySocket.read(30), MSG_ENCODING);   // ����ŷ���ȣ
                this.ACIpgumNm[i] = new String(this.KSPaySocket.read(20), MSG_ENCODING);   // �Ա��ڸ�
                this.ACBankFee[i] = new String(this.KSPaySocket.read(13), MSG_ENCODING);   // ������ü ������
                this.ACBankAmount[i] = new String(this.KSPaySocket.read(13), MSG_ENCODING);   // �Ѱ����ݾ�(�������ݾ�+ ������
                this.ACBankRespCode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);   // �����ڵ�
                this.ACMessage1[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���� message 1
                this.ACMessage2[i] = new String(this.KSPaySocket.read(16), MSG_ENCODING);   // ���� message 2
                this.ACCavvSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ��ȣȭ���������俩��
                this.ACFiller[i] = new String(this.KSPaySocket.read(183), MSG_ENCODING);   // ����

                String EncLen = "";
                this.ACEncData[i] = "";
                if (ACCavvSele[i].equals("1")) {
                    EncLen = new String(this.KSPaySocket.read(5), MSG_ENCODING);
                    this.ACEncData[i] = new String(this.KSPaySocket.read(Integer.parseInt(EncLen)), MSG_ENCODING); // �ݰ����ȣȭ����
                }

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.ACTransactionNo[i]);
                TmpReceiveMsg.append(this.ACStatus[i]);
                TmpReceiveMsg.append(this.ACTradeDate[i]);
                TmpReceiveMsg.append(this.ACTradeTime[i]);
                TmpReceiveMsg.append(this.ACAcctSele[i]);
                TmpReceiveMsg.append(this.ACFeeSele[i]);
                TmpReceiveMsg.append(this.ACInjaName[i]);
                TmpReceiveMsg.append(this.ACPareBankCode[i]);
                TmpReceiveMsg.append(this.ACPareAcctNo[i]);
                TmpReceiveMsg.append(this.ACCustBankCode[i]);
                TmpReceiveMsg.append(this.ACCustAcctNo[i]);
                TmpReceiveMsg.append(this.ACAmount[i]);
                TmpReceiveMsg.append(this.ACBankTransactionNo[i]);
                TmpReceiveMsg.append(this.ACIpgumNm[i]);
                TmpReceiveMsg.append(this.ACBankFee[i]);
                TmpReceiveMsg.append(this.ACBankAmount[i]);
                TmpReceiveMsg.append(this.ACBankRespCode[i]);
                TmpReceiveMsg.append(this.ACMessage1[i]);
                TmpReceiveMsg.append(this.ACMessage2[i]);
                TmpReceiveMsg.append(this.ACCavvSele[i]);
                TmpReceiveMsg.append(this.ACFiller[i]);

                if (EncLen.length() == 5) {
                    TmpReceiveMsg.append(EncLen);
                    TmpReceiveMsg.append(this.ACEncData[i]);
                }

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("AcctRequest_recv,appr(2200,2300,2420) ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
            // ���������� ��ȸ���
            else if (this.ApprovalType[i].substring(0, 2).equals("A7")) {
                this.SITransactionNo[i] = new String(this.KSPaySocket.read(12), MSG_ENCODING);   // �ŷ���ȣ
                this.SIStatus[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ����:O, ����: X
                this.SIRespCode[i] = new String(this.KSPaySocket.read(4), MSG_ENCODING);   // '0000' : ����ó��
                this.SIAgenMembDealSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ��ü���౸��
                this.SIStartSele[i] = new String(this.KSPaySocket.read(1), MSG_ENCODING);   // ���ÿ���
                this.SIEntrNumb[i] = new String(this.KSPaySocket.read(10), MSG_ENCODING);   // ����ڹ�ȣ
                this.SIShopName[i] = new String(this.KSPaySocket.read(30), MSG_ENCODING);   // ������
                this.SIMembNumbGene[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);   // �Ϲ� ��������ȣ
                this.SIMembNumbNoin[i] = new String(this.KSPaySocket.read(15), MSG_ENCODING);   // ������ ��������ȣ
                this.SIAlloMontType[i] = new String(this.KSPaySocket.read(200), MSG_ENCODING);   // �Һ�����
                this.SIFiller[i] = new String(this.KSPaySocket.read(207), MSG_ENCODING);   // ����

                TmpReceiveMsg = new StringBuffer();

                TmpReceiveMsg.append(this.SITransactionNo[i]);
                TmpReceiveMsg.append(this.SIStatus[i]);
                TmpReceiveMsg.append(this.SIRespCode[i]);
                TmpReceiveMsg.append(this.SIAgenMembDealSele[i]);
                TmpReceiveMsg.append(this.SIStartSele[i]);
                TmpReceiveMsg.append(this.SIEntrNumb[i]);
                TmpReceiveMsg.append(this.SIShopName[i]);
                TmpReceiveMsg.append(this.SIMembNumbGene[i]);
                TmpReceiveMsg.append(this.SIMembNumbNoin[i]);
                TmpReceiveMsg.append(this.SIAlloMontType[i]);
                TmpReceiveMsg.append(this.SIFiller[i]);

                this.ReceiveMsg += TmpReceiveMsg.toString();
                System.out.println("ShopInfoDetail(A700) ReceiveMsg[" + i + "]" + "=[" + TmpReceiveMsg.toString() + "]");
            }
        }
        return true;
    }


    public boolean SendSocket(String Flag) {
        int state_flag;     /*���������÷���*/
        String real_send_msg = "";

        try {
            real_send_msg = this.HeadMsg + this.SendMsg;

            String pDataLen = this.format("" + real_send_msg.getBytes(MSG_ENCODING).length, 4, '9');
            real_send_msg = pDataLen + real_send_msg;

            System.out.println(">>>>>>>  SendSocket Start~!! <<<<<<<<");
            System.out.println("SendMessage=[" + real_send_msg + "]");

            state_flag = 9;
			/*
				state_flag = 0 ����
				state_flag = 1 FEP����Ž���
				state_flag = 2 BackUrl���⿡ ������ ����Ҹ� �Ͽ���.
				state_flag = 3 BackUrl���⿡ ������ ����Ҹ� �Ͽ����� ��ҽ����Ͽ���.
				state_flag =
			*/

            if (!this.ProcessRequest(this.IPAddr, this.Port, Flag, real_send_msg))    //FEP�� ��Ž����ǰ��
            {
                state_flag = 1;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            System.out.println("���ο�û ����");
            return false;
        }

        /*���ܻ�Ȳ(��Ž���,BU�����������)*/
        if (state_flag == 1) /*FEP����Ž���*/ {
            for (int i = 0; i < this.ReceiveCount; i++) {
                Status[i] = "X";
                Message1[i] = "KSPAY����Ž���";    // �޽���1
                Message2[i] = "�������õ�";       // �޽���2
                Point1[i] = "000000000000";
                Point2[i] = "000000000000";
                Point3[i] = "000000000000";
                Point4[i] = "000000000000";

                VAStatus[i] = "X";
                VAMessage1[i] = "KSPAY����Ž���";    // �޽���1
                VAMessage2[i] = "�������õ�";       // �޽���2

                WPStatus[i] = "X";
                WPMessage1[i] = "KSPAY����Ž���";    // �޽���1
                WPMessage2[i] = "�������õ�";       // �޽���2
                WPAuthNo[i] = "9999";
                WPBalanceAmount[i] = "000000000";
                WPLimitAmount[i] = "000000000";

                PStatus[i] = "X";
                PMessage1[i] = "KSPAY����Ž���";    // �޽���1
                PMessage2[i] = "�������õ�";       // �޽���2
                PPoint1[i] = "000000000";
                PPoint2[i] = "000000000";
                PPoint3[i] = "000000000";
                PPoint4[i] = "000000000";

                HStatus[i] = "X";
                HMessage1[i] = "KSPAY����Ž���";    // �޽���1
                HMessage2[i] = "�������õ�";       // �޽���2

                MB1Status[i] = "X";
                MB1Message[i] = "KSPAY����Ž���";     // �޽���

                MB2Status[i] = "X";
                MB2Message[i] = "KSPAY����Ž���"; // �޽���

                BINStatus[i] = "X";
                BINMessage1[i] = "KSPAY����Ž���";     // �޽���1
                BINMessage2[i] = "�������õ�";        // �޽���2
            }
        }

        return true;
    }

    private boolean ProcessRequest(String addr, int port, String ServiceType, String SendMsg) throws IOException {
        boolean ret = false;

        this.KSPaySocket = new KSPaySocketBean(addr, port);

        this.KSPaySocket.ConnectSocket();   //IPG_Server�� ������ �δ´�
        this.KSPaySocket.write(SendMsg.getBytes(MSG_ENCODING)); //IPG_Server�� ����/��ҿ�û ����Ÿ�� ������.

        ret = ReceiveMessage();

        this.KSPaySocket.CloseSocket();

        return ret;
    }

    public static String format(String str, int len, char ctype) {
        byte[] buff;
        int filllen = 0;

        String trim_str = null;
        StringBuffer sb = new StringBuffer();

        try {
            buff = (str == null) ? new byte[0] : str.getBytes(MSG_ENCODING);

            filllen = len - buff.length;
            if (filllen < 0) {
                for (int i = 0, j = 0; j < len - 4; i++)//������ ������ �ΰ� �߶������
                {
                    j += (str.charAt(i) > 127) ? 2 : 1;
                    sb.append(str.charAt(i));
                }

                trim_str = sb.toString();
                buff = trim_str.getBytes(MSG_ENCODING);
                filllen = len - buff.length;

                if (filllen <= 0) return new String(buff, 0, len, MSG_ENCODING);//����� ����� ��Ÿ����...
                sb.setLength(0);
            } else {
                trim_str = str;
            }

            if (ctype == '9')   // ���ڿ��� ���
            {
                for (int i = 0; i < filllen; i++) sb.append('0');
                sb.append(trim_str);
            } else              // ���ڿ��� ���
            {
                for (int i = 0; i < filllen; i++) sb.append(' ');
                sb.insert(0, trim_str);
            }
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
        }
        return sb.toString();
    }

    private String setTrim(String str, int len) {
        byte[] subbytes = null;
        String tmpStr = null;
        subbytes = new byte[len];

        try {
            System.arraycopy(str.getBytes(MSG_ENCODING), 0, subbytes, 0, len);
            tmpStr = new String(subbytes, MSG_ENCODING);
            if (tmpStr.length() == 0) {
                subbytes = new byte[len - 1];
                System.arraycopy(str.getBytes(MSG_ENCODING), 0, subbytes, 0, len - 1);
                tmpStr = new String(subbytes, MSG_ENCODING);
            }
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
        }
        return tmpStr;
    }

    private String setLogMsg(String str) {
        String strBuf = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).equals(" "))
                strBuf = strBuf + "_";
            else
                strBuf = strBuf + str.substring(i, i + 1);
        }
        return strBuf;
    }

    public static String[] split(String srcStr, char c1) {
        return split(srcStr, String.valueOf(c1));
    }

    public static String[] split(String srcStr, String str1) {
        if (srcStr == null) return new String[0];

        String[] tokenArr = null;
        if (srcStr.indexOf(str1) == -1) {
            tokenArr = new String[1];
            tokenArr[0] = srcStr;

            return tokenArr;
        }

        LinkedList linkedlist = new LinkedList();

        int srcLength = srcStr.length();
        int tockenLength = str1.length();

        int pos = 0, startPos = 0;
        while (startPos < srcLength) {
            pos = srcStr.indexOf(str1, startPos);

            if (-1 == pos) break;

            linkedlist.add(srcStr.substring(startPos, pos));
            startPos = pos + tockenLength;
        }

        if (startPos <= srcLength) linkedlist.add(srcStr.substring(startPos));

        return (String[]) linkedlist.toArray(new String[0]);
    }//split
}