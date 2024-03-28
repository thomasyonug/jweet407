class CookAndCustomer {
	static main(args) {
		
		const cook = new Cook();
		const customer = new Customer();
		cook.start();
		customer.start();
	}
}
CookAndCustomer["__class"] = "CookAndCustomer";

class Desk {
	static food_flag = 0;

	static count = 10;

	static lock = new Object();
}

// const Desk = buildProxy(__Desk);

class Cook extends WebWorker {
	debugger
	// __captured_cvs = { 'Desk.count': Desk.count, 'Desk.lock': Desk.lock, 'Desk.food_flag': Desk.food_flag, }
	source = `
class __Desk {

	static food_flag = 0;

	static count= 10;

}

// Desk["__class"] = "Desk";
var Desk = buildProxy(__Desk);
Desk.lock = new Object();
class Cook {
	
	run() {
			while((true)) {{
					Comm.sync(Desk.lock);
					{		
							if (Desk.count === 0){
									Comm.unsync(Desk.lock);
									break;
							} else {
									if (Desk.food_flag === 1){
											try {
													Comm.sync(Desk.lock);
													Comm.wait(Desk.lock);
													Comm.unsync(Desk.lock);
											} catch(e) {
											}
									} else {
											console.info("\u53a8\u5e08\u505a\u996d");
											Desk.food_flag = 1;
									}
									Comm.notify(Desk.lock);
									Comm.unsync(Desk.lock);
							}
					};
			}};
			console.info("cook end");
	}
}
Cook["__class"] = "Cook";
var __entry = new Cook(); __entry.run();
`}

class Customer extends WebWorker {
	debugger
	source = `
class __Desk {

	static food_flag = 0;

	static count = 10;
}
// Desk["__class"] = "Desk";
var Desk = buildProxy(__Desk);
Desk.lock = new Object();
class Customer {
	run() {
			while((true)) {{
					Comm.sync(Desk.lock);
						console.log(Desk.count);
						if (Desk.count === 0){
								Comm.unsync(Desk.lock);
								break;
						} else {
								if (Desk.food_flag === 0){
										try {
												Comm.wait(Desk.lock);
										} catch(e) {
										}
								} else {
										Desk.count--;
										console.log("\u5ba2\u6237\u5403\u996d");
										Desk.food_flag = 0;
								}
								Comm.notify(Desk.lock);
								Comm.unsync(Desk.lock);
						}
			}};
	}
}
Customer["__class"] = "Customer";
var __entry = new Customer(); __entry.run();
console.log("customer end");
`}

CookAndCustomer.main(null);