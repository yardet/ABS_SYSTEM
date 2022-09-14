package chat.utils;

import engine.chat.ChatManager;
import engine.users.UserManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import services.main_service.Engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static chat.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
	private static final String ABS_MANAGER_ATTRIBUTE_NAME = "ABSManager";
	public final static String ADMIN_PASSWORD = "1234";


	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object chatManagerLock = new Object();
	private static final Object ABSManagerLock = new Object();


	public static void addNewEngineAndSaveOldEngine(ServletContext servletContext, int currentYaz) {

		Engine engine = getABSManager(servletContext);
		servletContext.setAttribute(String.format("%s %d", ABS_MANAGER_ATTRIBUTE_NAME, currentYaz), engine);

		synchronized (ABSManagerLock) {
			Engine newEngine= new Engine(engine);
			newEngine.promotingTimeline();
			servletContext.setAttribute(ABS_MANAGER_ATTRIBUTE_NAME, newEngine);
			Object realYazObj = servletContext.getAttribute("realYAZ");
			if(realYazObj==null)
				servletContext.setAttribute("realYAZ",1);
			else {
				int realYaz = (int) realYazObj + 1;
				servletContext.setAttribute("realYAZ", realYaz);
			}


		}

	}
	public static void updateEngineToAnotherYazEngine(ServletContext servletContext, int currentYaz) {
		Engine engine = getABSManager(servletContext);
		if(engine.getCurrentYaz() > currentYaz)
			servletContext.setAttribute(String.format("%s %d", ABS_MANAGER_ATTRIBUTE_NAME, engine.getCurrentYaz()), engine);

		synchronized (ABSManagerLock) {
			Object obj = servletContext.getAttribute(String.format("%s %d", ABS_MANAGER_ATTRIBUTE_NAME, currentYaz));
			if (obj != null) {
				servletContext.setAttribute(ABS_MANAGER_ATTRIBUTE_NAME, obj);
				Object realYazObj = servletContext.getAttribute("realYAZ");
				if(realYazObj==null)
					servletContext.setAttribute("realYAZ",1);

			}
		}

	}

	public static Engine getABSManager(ServletContext servletContext) {

		synchronized (ABSManagerLock) {
			if (servletContext.getAttribute(ABS_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ABS_MANAGER_ATTRIBUTE_NAME, new Engine());
				servletContext.setAttribute("realYAZ",1);
			}
		}
		return (Engine) servletContext.getAttribute(ABS_MANAGER_ATTRIBUTE_NAME);
	}

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static String getBody(HttpServletRequest request) throws IOException {

		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		return body;
	}


	public static ChatManager getChatManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
			}
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}

	public static boolean isRewind(ServletContext servletContext, int currentYaz) {
		Object realYazObj = servletContext.getAttribute("realYAZ");
		if(realYazObj==null) {
			servletContext.setAttribute("realYAZ", 1);
			return false;
		}
		return currentYaz < (int)realYazObj;

	}
}
