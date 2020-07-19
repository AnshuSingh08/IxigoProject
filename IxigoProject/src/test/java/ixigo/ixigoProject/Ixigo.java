package ixigo.ixigoProject;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
//import resources.base;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class Ixigo {

	public static Logger logger = LogManager.getLogger(Ixigo.class.getName());

	WebDriver driver = new ChromeDriver();

	public Ixigo() {
		// this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//input[@placeholder='Enter city or airport']")
	WebElement from;

	@FindBy(xpath = "(//input[@placeholder='Enter city or airport'])[2]")
	WebElement destination;

	@Test
	public void login() throws InterruptedException {
		driver.get("https://www.ixigo.com"); // URL in the browser
		driver.manage().window().maximize();
		// driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();

		String windowtitle = driver.getTitle();
		System.out.println("windowtitle:" + windowtitle);
		Assert.assertTrue(windowtitle.contains("ixigo"));
		System.out.println("ixigo page validated");

		WebDriverWait wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[text()='Round Trip']"))));

		// select round trip
		driver.findElement(By.xpath("//div[text()='Round Trip']")).click();

		Thread.sleep(3000);

		from.clear();
		if (driver.findElement(By.xpath("//div[@class='clear-input ixi-icon-cross']")).isDisplayed()) {
			driver.findElement(By.xpath("//div[@class='clear-input ixi-icon-cross']")).click();
			System.out.println("depart field is not empty");
		}
		
		
		from.sendKeys("PUNE");
		Thread.sleep(3000);

		from.sendKeys(Keys.ENTER);

		System.out.println("pune inserted:" + from.getAttribute("value"));
		Assert.assertTrue(from.getAttribute("value").contains("Pune"));
		System.out.println("Pune selected");
		// driver.findElement(By.te)
		// destination.clear();

		destination.sendKeys("Hyderabad");
		Thread.sleep(4000);
		destination.sendKeys(Keys.ENTER);
		System.out.println("hyderabad inserted:" + destination.getAttribute("value"));
		Assert.assertTrue(destination.getAttribute("value").contains("Hyderabad"));
		System.out.println("Hyderabad selected");

		// TABLE DATE 17 sep
		driver.findElement(By.xpath("//input[@placeholder='Depart']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@class='ixi-icon-arrow rd-next']")).click();

		// Search september month
		WebElement sepMonth = driver.findElement(By.xpath("//div[text()='September 2020']"));
		System.out.println("sep selected");
		List<WebElement> tableRows = sepMonth.findElements(By.xpath("//table[@class='rd-days']/tbody/tr"));

		//System.out.println("table row size" + tableRows.size());

		// select departure date
		WebElement departDate = tableRows.get(3).findElement(By.xpath("//td[@data-date='17092020']/div[1]"));
		departDate.click();

		// executor.executeScript("arguments[0].click();", departDate);

		System.out.println("17 sep clicked");
		Thread.sleep(2000);

		WebElement passengers = driver.findElement(By.xpath("//div[text()='Travellers | Class']"));
		Actions act = new Actions(driver);
		act.moveToElement(passengers).build().perform();

		driver.findElement(By.xpath("//div[text()='Travellers | Class']")).click();

		driver.findElement(By.xpath("//span[@data-val='2']")).click();
		System.out.println("return 2");

		driver.findElement(By.xpath("//div[@class='close-btn u-pos-abs ixi-icon-cross']")).click();

		driver.findElement(By.xpath("//*[@placeholder='Return']")).click();
		// Thread.sleep(2000);
		System.out.println("return clicked");
		Thread.sleep(2000);

		WebElement nextmonth = driver.findElement(By.xpath("//button[@class='ixi-icon-arrow rd-next']"));

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", nextmonth);

		// Thread.sleep(6000);
		WebElement octMonth = driver.findElement(By.xpath("//div[text()='October 2020']"));

		List<WebElement> octtableRows = octMonth.findElements(By.xpath("//table[@class='rd-days']/tbody/tr"));

		// System.out.println("october table size"+octtableRows.size());
		// Thread.sleep(2000);

		// select departure date

		WebElement returnDate = octtableRows.get(4).findElement(By.xpath("//td[@data-date='24102020']/div[1]"));
		driver.findElement(By.xpath("//*[@placeholder='Return']")).click();
		// JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", returnDate);

		System.out.println("24 oct clicked");

		// search click
		driver.findElement(By.xpath("//button[@class='c-btn u-link enabled']")).click();
		System.out.println("search click");

		WebDriverWait waitNew = new WebDriverWait(driver, 400);
		waitNew.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='stops']")));

		WebElement stopfilter = driver.findElement(By.xpath("//div[@class='stops']"));
		WebElement departure = driver.findElement(By.xpath("//div[@class='fltr tmng']"));
		WebElement Airlines = driver.findElement(By.xpath("//div[@class='fltr u-pos-rel arln']"));

		Assert.assertTrue(stopfilter.isDisplayed());
		Assert.assertTrue(departure.isDisplayed());
		Assert.assertTrue(Airlines.isDisplayed());

		// select non stop
		stopfilter.findElement(By.xpath("//span/div[1]/span[1]/span")).click();

		// Departure date details***********************
		List<WebElement> divs = driver.findElements(By.xpath("//div[@class='result-col-inner']/div[1]/div"));
		//System.out.println("depart results size:" + divs.size());

		String price = "";
		String airlines = "";
		String departTime = "";
		for (int i = 1; i <= divs.size(); i++) {
			price = driver.findElement(By.xpath("//div[@class='result-col-inner']/div[" + i + "]/div[1]/div[5]"))
					.getText();
			int departamount = Integer.valueOf(price);
			if (departamount < 5000) {
				airlines = driver
						.findElement(By.xpath("//div[@class='result-col-inner']/div[" + i + "]/div[1]/div[3]/div[4]"))
						.getText();
				departTime = driver
						.findElement(By.xpath("//div[@class='result-col-inner']/div[" + i + "]/div[1]/div[3]/div[1]"))
						.getText();
				System.out.println("Depart details flight < 5000:"+i);
				System.out.println("Airlines name:" + airlines + ", Fare:" + price + ", departTime:" + departTime);
			}
		}

		// Return date details***********************
		List<WebElement> divReturn = driver.findElements(By.xpath("//*[@class='result-col']/div/div"));

		//System.out.println("Return results size:" + divReturn.size());

		String returnprice = "";
		String returnairlines = "";
		String returnTime = "";
		for (int i = 1; i <= divReturn.size(); i++) {
			returnprice = driver.findElement(By.xpath("//div[@class='result-col']/div/div[" + i + "]/div[1]/div[5]"))
					.getText();
			int returnamount = Integer.valueOf(returnprice);
			if (returnamount < 5000) {

				returnairlines = driver
						.findElement(By.xpath("//div[@class='result-col']/div/div[" + i + "]/div[1]/div[3]/div[4]"))
						.getText();
				returnTime = driver
						.findElement(By.xpath("//div[@class='result-col']/div/div[" + i + "]/div[1]/div[3]/div[1]"))
						.getText();
				System.out.println("Return details flight < 5000:"+i);
				System.out.println(
						"Airlines name:" + returnairlines + ", Fare:" + returnprice + ", departTime:" + returnTime);
			}
		}
		
		driver.findElement(By.xpath("//div[@class='result-col-inner']/div[1]/div[1]")).click();
		//System.out.println("depart results size:" + divs.size());

		//click on book
		driver.findElement(By.xpath("//div[@class='book-cta']")).click();
		System.out.println("book button clicked");
		Thread.sleep(2000);
		WebDriverWait waitBaggagePage = new WebDriverWait(driver, 200);
		waitBaggagePage.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='fare-dtl']"))));
		System.out.println("Fare displayed successfully");
		System.out.println("Continue after login");
		

		driver.close();
	}
}
