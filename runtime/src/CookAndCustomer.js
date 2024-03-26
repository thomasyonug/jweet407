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
					console.log("cook before sync");
					Comm.sync(Desk.lock);
					console.log("cook after sync");
					{		
							console.log("before sync in sync");
							Comm.sync(Desk.lock);
							console.log("after sync in sync");
							console.log("before unsync in sync");
							Comm.unsync(Desk.lock);
							console.log("after unsync in sync");
							console.log(Desk.count);
							if (Desk.count === 0){
									Comm.unsync(Desk.lock);
									break;
							} else {
									if (Desk.food_flag === 1){
											try {
													console.info("cook before wait");
													Comm.wait(Desk.lock);
													console.info("cook after wait");
											} catch(e) {
											}
									} else {
											console.info("\u53a8\u5e08\u505a\u996d");
											Desk.food_flag = 1;
									}
									console.info("cook before notify");
									Comm.notify(Desk.lock);
									console.info("cook after notify");
									console.log("cook before unsync");
									Comm.unsync(Desk.lock);
									console.log("cook after unsync");
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
						console.log("customer before sync");
					Comm.sync(Desk.lock);
						console.log("customer after sync");
						console.log(Desk.count);
						if (Desk.count === 0){
								Comm.unsync(Desk.lock);
								break;
						} else {
								if (Desk.food_flag === 0){
										try {
												console.log("customer before wait");
												Comm.wait(Desk.lock);
												console.log("customer after wait");
										} catch(e) {
										}
								} else {
										Desk.count--;
										console.log("\u5ba2\u6237\u5403\u996d");
										Desk.food_flag = 0;
								}
								console.log("customer before notify");
								Comm.notify(Desk.lock);
								console.log("customer after notify");
								console.log("customer before unsync");
								Comm.unsync(Desk.lock);
								console.log("customer after unsync");
						}
			}};
		console.log("customer end");
	}
}
Customer["__class"] = "Customer";
var __entry = new Customer(); __entry.run();
`}

CookAndCustomer.main(null);