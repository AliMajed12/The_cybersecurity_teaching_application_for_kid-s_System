<%@ WebService Language="C#" Class="Service" %>

using System;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Net.Mail;
using System.Text;
using System.Security.Cryptography;
using System.IO;
using System.Drawing;



[WebService(Namespace = "http://securitylessons-001-site1.btempurl.com/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]
public class Service : System.Web.Services.WebService {
    [WebMethod]
    public string HelloWorld() {
        return "Hello ";
    }

    SqlCommand Command;
    public Service() {

        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
        //http://securitylessons-001-site1.btempurl.com/

    }

    public static string EncryptString(string key, string plainText)
    {
        byte[] iv = new byte[16];
        byte[] array;

        using (Aes aes = Aes.Create())
        {
            aes.Key = Encoding.UTF8.GetBytes(key);
            aes.IV = iv;

            ICryptoTransform encryptor = aes.CreateEncryptor(aes.Key, aes.IV);

            using (MemoryStream memoryStream = new MemoryStream())
            {
                using (CryptoStream cryptoStream = new CryptoStream((Stream)memoryStream, encryptor, CryptoStreamMode.Write))
                {
                    using (StreamWriter streamWriter = new StreamWriter((Stream)cryptoStream))
                    {
                        streamWriter.Write(plainText);
                    }

                    array = memoryStream.ToArray();
                }
            }
        }

        return Convert.ToBase64String(array);
    }

    public static string DecryptString(string key, string cipherText)
    {
        byte[] iv = new byte[16];
        byte[] buffer = Convert.FromBase64String(cipherText);

        using (Aes aes = Aes.Create())
        {
            aes.Key = Encoding.UTF8.GetBytes(key);
            aes.IV = iv;
            ICryptoTransform decryptor = aes.CreateDecryptor(aes.Key, aes.IV);

            using (MemoryStream memoryStream = new MemoryStream(buffer))
            {
                using (CryptoStream cryptoStream = new CryptoStream((Stream)memoryStream, decryptor, CryptoStreamMode.Read))
                {
                    using (StreamReader streamReader = new StreamReader((Stream)cryptoStream))
                    {
                        return streamReader.ReadToEnd();
                    }
                }
            }
        }
    }

    // Login1 Function for login into Database and APPLICATION............

    [WebMethod]
    public string LoginCheck1(String UserName, String Password,String UserType)
    {
        try
        {
            string getUserID;
            string sqlupdatecode = "";

            string list = "";
            switch (UserType)
            {
                case "Administrator":
                    getUserID = "select  AdminID , Email from dbo.Administrator where UserName=@UserName and Password=@Password ";

                    break;
                case "Children":
                    getUserID = "select  ChildID  ,Email from dbo.Children where UserName=@UserName and Password=@Password ";

                    break;

                default:
                    getUserID = "select  ParentID  ,Email from dbo.Parent where UserName=@UserName and Password=@Password ";

                    break;
            }

            string code = new Random().Next(10005, 99000).ToString();
            SqlCommand command = JoinClass.CreateCommand(getUserID, "sql");
            command.Parameters.AddWithValue("@UserName", UserName);
            command.Parameters.AddWithValue("@Password", Password);

            ArrayList rows = new ArrayList();
            DataTable dt;
            dt = JoinClass.GetDataSet(command).Tables[0];

            if (dt != null && dt.Rows.Count > 0)
            {


                foreach (DataRow dataRow in dt.Rows)
                {
                    list = string.Join(";", dataRow.ItemArray.Select(item => item.ToString()));

                }
                list = list.Insert(0, "Succeed;");



                SendActiveCode(dt.Rows[0][1].ToString(), code);
                sqlupdatecode = "update dbo."+UserType+" set Verification=@code where Email=@Email";
                command = JoinClass.CreateCommand(sqlupdatecode, "sql");
                command.Parameters.AddWithValue("@code", code);
                command.Parameters.AddWithValue("@Email", dt.Rows[0][1].ToString());
                command.ExecuteNonQuery();
            }
            else
            {
                //rows.Add("Fail");
                list = "Fail";
            }
            return list;




        }
        catch (Exception ex)
        {
            return ex.Message;
        }


    }

    //For Send Code Across Email
    [WebMethod]
    public String SendActiveCode(String Email, String Code)
    {
        try
        {


            System.Net.ServicePointManager.SecurityProtocol = System.Net.SecurityProtocolType.Tls12;
            //create the mail message 
            MailMessage mailMessage = new MailMessage();

            //set the addresses 
            mailMessage.From = new MailAddress("securitylessons@maintainceserver.us");
            mailMessage.To.Add(Email);

            //set the content 
            mailMessage.Subject = "Welcome In Securty lessons \n ActiveCode \n";
            mailMessage.IsBodyHtml = true;
            mailMessage.Body = "The Active Code is:" + Code;
            //send the message 
            SmtpClient smtpClient = new SmtpClient("mail5011.site4now.net");
            // SmtpClient smtpClient = new SmtpClient("mail5011.site4now.net"); 


            smtpClient.UseDefaultCredentials = false;
            // smtpClient.Credentials = Credentials;
            smtpClient.Port = 8889;    //alternative port number is 8889  25    ssl 465

            smtpClient.EnableSsl = false;
            smtpClient.Credentials = new System.Net.NetworkCredential("securitylessons@maintainceserver.us", "Hh@20232320");
            try
            {
                smtpClient.Send(mailMessage);

            }
            catch (SmtpException ex)
            {

                return Code +"http"+Email+" :   "+ ex.Message+ex.StackTrace;            }

            return "hello";
        }
        catch (Exception ex)
        {
            return Code +"?:"+Email+" :   "+ ex.Message+ex.StackTrace;

        }

    }

    // Login1 Function for login into Database and APPLICATION............

    [WebMethod]
    public string LoginCheck2(String UserType,String UserID, String Code)
    {
        try
        {
            string getUserID;
            object objcheck;
            switch (UserType)
            {
                case "Administrator":
                    getUserID = "select  FullName from dbo.Administrator where AdminID=@UserID and Verification=@Code ";

                    break;
                case "Parent":
                    getUserID = "select  FullName   from dbo.Parent where ParentID=@UserID and Verification=@Code ";

                    break;

                default:
                    getUserID = "select  FullName  from dbo.Children where ChildID=@UserID and Verification=@Code ";

                    break;
            }


            SqlCommand command = JoinClass.CreateCommand(getUserID, "sql");
            command.Parameters.AddWithValue("@UserID", UserID);
            command.Parameters.AddWithValue("@Code", Code);
            objcheck = command.ExecuteScalar();
            if (objcheck != null && objcheck.ToString() != String.Empty)
            {

                return "Succeed;" + objcheck.ToString();

            }
            else
            {
                return "Fail;";

            }





        }
        catch (Exception ex)
        {
            return "Fail;" + ex.Message;
        }


    }

    //Register  New User
    #region  New User
    [WebMethod]
    public string RegisterUser(String FullName, String DOB, String Email
,String Mobile, String Gender
, String Address,String UserName,String Password, String UserType)
    {
        try
        {
            switch (UserType)
            {
                case "Children":
                    UserType = "Children";
                    break;
                case "Administrator":
                    UserType = "Administrator";
                    break;
                default:
                    UserType = "Parent";
                    break;
            }
            bool res = JoinClass.checkHasData(JoinClass.CreateCommand(@"select UserName from [dbo]."+UserType+" where [UserName]='" + UserName + "'", "sql"));
            if (res == true)
            {
                return "FoundUser";
            }
            bool resEmail = JoinClass.checkHasData(JoinClass.CreateCommand(@"select UserName from [dbo]."+UserType+" where [Email]='" + Email + "'", "sql"));

            //***

            if (resEmail == true)
            {
                return "FoundEmail";
            }


            string sql = @"INSERT INTO [dbo]."+UserType+@"(
            FullName
            ,[DOB]
            ,[Email]
            ,[Mobile]
            ,[Gender]
            ,Address
            ,UserName
            ,[Password]
            ,Accept
         )
         VALUES
           (
            @FullName
            ,@DOB
            ,@Email
            ,@Mobile
            ,@Gender
            ,@Address
            ,@UserName
            ,@Password
            ,0

		    )";
            Command = JoinClass.CreateCommand(sql, "sql");
            Command.Parameters.AddWithValue("@FullName", FullName);
            Command.Parameters.AddWithValue("@DOB", DOB);
            Command.Parameters.AddWithValue("@Email", Email);
            Command.Parameters.AddWithValue("@Mobile", Mobile);
            Command.Parameters.AddWithValue("@Gender", Gender);
            Command.Parameters.AddWithValue("@Address", Address);
            Command.Parameters.AddWithValue("@UserName", UserName);
            Command.Parameters.AddWithValue("@Password",  Password);

            int result = Command.ExecuteNonQuery();
            if (result > 0)
            {

                return "Succeed";
            }
            else
            {
                return "Fail";
            }
            //********


        }
        catch (Exception ex)
        {

            return "Fail" + ex.Message;
        }

    }
    #endregion
 
    [WebMethod]

    public ArrayList displayDataOfParent()
    {
        ArrayList rows = new ArrayList();
        DataTable dt;
        string sql = @"select [ParentID],FullName+' ' , Email,Mobile,Accept'  '  from [dbo].[Parent]  ";
        dt = JoinClass.GetDataSet(JoinClass.CreateCommand(sql, "sql")).Tables[0];


        foreach (DataRow dataRow in dt.Rows)
            rows.Add(string.Join(";", dataRow.ItemArray.Select(item => item.ToString())));

        return rows;

    }

    //Display Data of Clients evaluation
    [WebMethod]

    public ArrayList displayDataOfChildEvaluation()
    {
        ArrayList rows = new ArrayList();
        DataTable dt;
        string sql = @"select [ClientID],[FullName],[Mobile],[PersonalIdentity],[rate] from  [dbo].[View_EvalutionsOfClients]";
        dt = JoinClass.GetDataSet(JoinClass.CreateCommand(sql, "sql")).Tables[0];


        foreach (DataRow dataRow in dt.Rows)
            rows.Add(string.Join(";", dataRow.ItemArray.Select(item => item.ToString())));

        return rows;

    }
   


    [WebMethod]

    public ArrayList displayDataOfChild()
    {
        ArrayList rows = new ArrayList();
        DataTable dt;
        string sql = @"select [ChildID],FullName+' ' , Email,Mobile,Accept'  '  from [dbo].[Children]  ";
        dt = JoinClass.GetDataSet(JoinClass.CreateCommand(sql, "sql")).Tables[0];


        foreach (DataRow dataRow in dt.Rows)
            rows.Add(string.Join(";", dataRow.ItemArray.Select(item => item.ToString())));

        return rows;

    }
  

    //Display Data of Admin
    [WebMethod]

    public ArrayList displayDataOfAdmin()
    {
        ArrayList rows = new ArrayList();
        DataTable dt;
        string sql = @"select [AdminID],FullName+' ' , Email,Mobile,Accept'  '  from [dbo].[Administrator]  ";
        dt = JoinClass.GetDataSet(JoinClass.CreateCommand(sql, "sql")).Tables[0];


        foreach (DataRow dataRow in dt.Rows)
            rows.Add(string.Join(";", dataRow.ItemArray.Select(item => item.ToString())));

        return rows;

    }
    //Delete Admin
    [WebMethod]
    public string deleteAdmin(String UserID)
    {
        string sql = @"delete from [dbo].[AdminTB] Where [AdminID]=@UserID ";
        Command = JoinClass.CreateCommand(sql, "sql");
        Command.Parameters.AddWithValue("@UserID", UserID);
        int result = Command.ExecuteNonQuery();
        if (result > 0)
        {
            return "Succeed";
        }
        else
        {
            return "Fail";
        }

    }

 
    //display Data Of Lessons Patient
    [WebMethod]

    public ArrayList displayDataOfLessonsParent()
    {
        try
        {
            ArrayList rows = new ArrayList();
            DataTable dt;
            string sql = @"select [dbo].[Lessons].[LessonID],[dbo].[Lessons].[LessonName],[dbo].[Lessons].[Details],
[dbo].[Lessons].[Image1],ISNULL([dbo].[Evaluation].[Value],0) as [Value] from [dbo].[Lessons]
left outer join [dbo].[Evaluation] on [dbo].[Evaluation].LessonID=[dbo].[Lessons].LessonID;" ;
            dt = JoinClass.GetDataSet(JoinClass.CreateCommand(sql, "sql")).Tables[0];


            foreach (DataRow dataRow in dt.Rows)
                rows.Add(string.Join(";", dataRow.ItemArray.Select(item => item.ToString())));

            return rows;
        }
        catch (Exception ex)
        {
            return null;
        }

    }

//addlessons

    [WebMethod]
    public string addlessons( String LessonName,String Details,String Image1,String UserID )
    {
        try
        {
            string  fileName ="v"+ Guid.NewGuid() + ".jpeg";


            string path = Server.MapPath("pic/"+fileName);
            string pathurl = @"http://securitylessons-001-site1.btempurl.com/pic/" + fileName;

            //data:image/gif;base64,
            //this image is a single pixel (black)


            byte[] bytes = Convert.FromBase64String(Image1);

            Image image;
            using (MemoryStream ms = new MemoryStream(bytes))
            {

                image = Image.FromStream(ms);
                File.WriteAllBytes(path, bytes);
                // image.Save(path);
            }
            string sql = @"INSERT INTO [dbo]. Lessons(
            LessonName
            ,[Details]
            ,[Image1]
            ,AdminID
            )
         VALUES
           (
            @LessonName
            ,@Details
            ,@Image1
            ,@AdminID
          
		    )";
            Command = JoinClass.CreateCommand(sql, "sql");
                Command.Parameters.AddWithValue("@LessonName", LessonName);
            Command.Parameters.AddWithValue("@Details", Details);
            Command.Parameters.AddWithValue("@Image1", pathurl);
            Command.Parameters.AddWithValue("@AdminID", UserID);


            int result = Command.ExecuteNonQuery();
            if (result > 0)
            {

                return "Succeed";
            }
            else
            {
                return "Fail";
            }
            //********

        }
        catch (Exception ex)
        {

            return "Fail" + ex.Message+ex.InnerException.Message+ex.StackTrace;
        }

    }
        //addQuestions

    [WebMethod]
    public string addQuestion( String QuestionDetails,String QuestionDegree,String QuestionAnswer,String Image1,String LessonID )
    {
        try
        {
            string  fileName ="v"+ Guid.NewGuid() + ".jpeg";


            string path = Server.MapPath("pic/"+fileName);
            string pathurl = @"http://securitylessons-001-site1.btempurl.com/pic/" + fileName;

            //data:image/gif;base64,
            //this image is a single pixel (black)


            byte[] bytes = Convert.FromBase64String(Image1);

            Image image;
            using (MemoryStream ms = new MemoryStream(bytes))
            {

                image = Image.FromStream(ms);
                File.WriteAllBytes(path, bytes);
                // image.Save(path);
            }
            string sql = @"INSERT INTO [dbo]. LessonsQuestionsAnswers(
            QuestionDetails
            ,[QuestionImage]
            ,[QuestionDegree]
            ,QuestionAnswer
             ,LessonID
            )
         VALUES
           (
            @QuestionDetails
            ,@QuestionImage
            ,@QuestionDegree
            ,@QuestionAnswer
            ,@LessonID
          
		    )";
            Command = JoinClass.CreateCommand(sql, "sql");
            Command.Parameters.AddWithValue("@QuestionDetails", QuestionDetails);
            Command.Parameters.AddWithValue("@QuestionImage", pathurl);
            Command.Parameters.AddWithValue("@QuestionDegree", QuestionDegree);
            Command.Parameters.AddWithValue("@QuestionAnswer", QuestionAnswer);
            Command.Parameters.AddWithValue("@LessonID", LessonID);


            int result = Command.ExecuteNonQuery();
            if (result > 0)
            {

                return "Succeed";
            }
            else
            {
                return "Fail";
            }
            //********

        }
        catch (Exception ex)
        {

            return "Fail" + ex.Message+ex.InnerException.Message+ex.StackTrace;
        }

    }
    //dispaly data of All Lessons
    [WebMethod]

    public ArrayList displayDataOfAllLessons()
    {
        try
        {


            ArrayList rows = new ArrayList();
            DataTable dt;
            string sql = @"select [LessonID],[LessonName],[Details],[Image1],' '  from 
[dbo].[Lessons]";
            Command = JoinClass.CreateCommand(sql, "sql");

            dt = JoinClass.GetDataSet(Command).Tables[0];


            foreach (DataRow dataRow in dt.Rows)
                rows.Add(string.Join(";", dataRow.ItemArray.Select(item => item.ToString())));

            return rows;
        }
        catch (Exception)
        {

            return null;
        }

    }
 

    // Accept Admin
    [WebMethod]
    public string AcceptAdmin(String AdminID,String Allow)
    {
        try
        {
            int access = 0;
            if (Allow.Equals("True"))
            {
                access = 0;
            }else
            {
                access = 1;
            }
            string sql = @"Update [dbo].AdminTB set [Accept]=@Allow   where AdminID=@AdminID ";
            Command = JoinClass.CreateCommand(sql, "sql");
            Command.Parameters.AddWithValue("@AdminID", AdminID);
            Command.Parameters.AddWithValue("@Allow", access);



            int result = Command.ExecuteNonQuery();
            if (result > 0)
            {

                return "Succeed";
            }
            else
            {
                return "Fail";
            }


        }
        catch (Exception ex)
        {

            return "Fail" + ex.Message;
        }

    }
   
        //Evaluate

    [WebMethod]
    public string Evaluate(String Value, String Comment,String LessonID)
    {
        try
        {
            
          

            string sql = @"INSERT INTO [dbo].Evaluation([Comment]
           ,[Value]
           ,[LessonID]
           )
     VALUES
           (@Comment,
            @Value,
            @LessonID)";
            Command = JoinClass.CreateCommand(sql, "sql");
            Command.Parameters.AddWithValue("@Comment", Comment);
            Command.Parameters.AddWithValue("@Value", Value);
            Command.Parameters.AddWithValue("@LessonID", LessonID);
            int result = Command.ExecuteNonQuery();
            if (result > 0)
            {

                return "Succeed";
            }
            else
            {
                return "Fail";
            }


        }
        catch (Exception ex)
        {

            return "Fail" + ex.Message;
        }
    }



  

    /////for connect to sqlServer
    public class JoinClass
    {
        public JoinClass()
        {
            //
            // TODO: Add constructor logic here
            //
        }
        static DataSet set;
        static SqlDataAdapter adaptr;
        static string Connectionstr;
        static SqlConnection con;
        static SqlCommand Command;

        /// <summary>
        /// for open Connection and return Sqlconnection
        /// </summary>
        /// <returns> SqlConnection</returns>
        public static SqlConnection OpenConnection()
        {
            try
            {
                Connectionstr = "Data Source=SQL8004.site4now.net;Initial Catalog=db_a9707d_securitylessons;User Id=db_a9707d_securitylessons_admin;Password=Hh@20232320";
                con = new SqlConnection(Connectionstr);
                con.Open();

                return con;
            }
            catch (Exception)
            {
                return null;
            }
        }
        /// <summary>
        /// for create Sqlcommand
        /// </summary>
        /// <param name="commandtext"> name of procedure or the SqslStatment</param>
        /// <param name="type">StoredProcedur(proc)or SqlStatment(sql)</param>
        /// <returns>sqlCommand</returns>
        public static SqlCommand CreateCommand(string commandtext, string sql) {
            try
            {
                Command = new SqlCommand();
                Command.CommandText = commandtext;

                Command.CommandType = CommandType.Text;

                Command.Connection = OpenConnection();
                return Command;
            }
            catch (Exception) {

                return null;
            }
        }
        public static DataSet GetDataSet(SqlCommand Command)
        {

            try
            {
                set = new DataSet();
                adaptr = new SqlDataAdapter(Command);
                adaptr.Fill(set);
                return set;
            }

            catch (Exception)
            {

                return null;
            }
            finally
            {
                if (con != null)
                {
                    con.Dispose();
                }
                if (Command != null)
                {
                    Command.Dispose();
                }
            }


        }
        /// <summary>
        /// Return Object Of one row "ScalarValue"
        /// </summary>
        /// <param name="Command">SqlCommand</param>
        /// <returns>obj Scalar Value</returns>

        public static object GetScalarValue(SqlCommand Command)
        {

            try
            {
                object obj = Command.ExecuteScalar();
                return obj;
            }

            catch (Exception)
            {

                return null;
            }
            finally
            {
                if (con != null)
                {
                    con.Dispose();
                }
                if (Command != null)
                {
                    Command.Dispose();
                }
            }


        }



        /// <summary>
        /// Get  ID
        /// </summary>
        /// <param name="UserName">UserName</param>
        /// <param name="TableName">TableName</param>
        /// <returns>object</returns>
        public static object GetUserID(string ID, string UserName, string TableName)
        {
            try
            {


                string getUserID = "select " + ID + " from " + TableName + " where UserName=@UserName";
                SqlCommand command = CreateCommand(getUserID, "sql");
                command.Parameters.Add("@UserName", SqlDbType.NVarChar, 256).Value = UserName;

                object obj = command.ExecuteScalar();
                return obj;


            }
            catch (Exception)
            {

                return null;
            }
            finally
            {
                Command.Dispose();
            }

        }
        /// <summary>
        /// Login
        /// </summary>
        /// <param name="UserName"> </param>
        /// <param name="Password"> </param>
        /// TableName
        /// <returns>String</returns>
        public static string LoginSucceed(string UserName, string Password)
        {
            string getUserID;
            string obj = "";
            object objcheck;
            try
            {

                getUserID = "select RoleType ,UserID from dbo.Users where UserName=@UserName and Password=@Password ";


                SqlCommand command = CreateCommand(getUserID, "sql");
                command.Parameters.AddWithValue("@UserName", UserName);
                command.Parameters.AddWithValue("@Password", Password);
                objcheck = command.ExecuteScalar();
                if (objcheck == null)
                {
                    return "Fail";

                }
                string result = objcheck.ToString();
                if (result == string.Empty)
                {
                    return "Fail";
                }
                else
                {
                    return result;
                }



            }
            catch (Exception ex)
            {

                return ex.Message + ":" + ex.InnerException.Message;
            }
            finally
            {
                Command.Dispose();
            }

        }


        public DataSet ReturnDataSet(SqlCommand Command)
        {

            try
            {
                set = new DataSet();
                adaptr = new SqlDataAdapter(Command);
                adaptr.Fill(set);
                return set;
            }

            catch (Exception)
            {

                return null;
            }
            finally
            {
                if (con != null)
                {
                    con.Dispose();
                }
                if (Command != null)
                {
                    Command.Dispose();
                }
            }


        }


        /// <summary>
        /// Return Object Of one row "ScalarValue"
        /// </summary>
        /// <param name="Command">SqlCommand</param>
        /// <returns>obj Scalar Value</returns>
        public static bool checkHasData(SqlCommand Command)
        {

            try
            {
                DataSet set = GetDataSet(Command);
                if (set != null)
                {
                    if (set.Tables.Count > 0)
                    {
                        if (set.Tables[0].Rows.Count > 0)
                        {
                            return true;
                        }

                    }

                }
                return false;
            }

            catch (Exception)
            {

                return false;
            }
            finally
            {
                if (con != null)
                {
                    con.Dispose();
                }
                if (Command != null)
                {
                    Command.Dispose();
                }
            }


        }

        ///
    }
    ///////
}
