package parking;


import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;



public class BookingTest {


    private static final LocalDateTime START = LocalDateTime.of(2026, 3, 1, 9, 0);
    private static final LocalDateTime END = LocalDateTime.of(2026, 3, 1, 11, 0);


    private Vehicle vehicle;
    private ParkingSlot slot;
    private Booking booking;


    @BeforeAll
    public static void beforeAllTests() {
        System.out.println("Before All");
    }


    @BeforeEach
    public void CreateBooking() {
        System.out.println("Before Each");
        vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        booking = new Booking(7, vehicle, slot, START, END, 20.0);
        System.out.println(booking);
    }


    @AfterEach
    public void afterEachTest() {
        System.out.println("After Each");
    }


    @AfterAll
    public static void afterAllTests() {
        System.out.println("After All");
    }


    // constructor
    // @Test
    // public void ConstructorTest(){
    //Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
    //ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
    //Booking booking = new Booking(7, vehicle, slot, START, END, 20.0);


    // assertEquals(7, booking.getBookingId());
    // assertSame(vehicle, booking.getVehicle());
    // assertSame(slot, booking.getParkingSlot());
    //  assertEquals(START, booking.getStartTime());
    //  assertEquals(END, booking.getEndTime());
    //   assertEquals(20.0, booking.getAmount());
    //   assertEquals(BookingStatus.ACTIVE, booking.getBookingStatus());
    //  }


    //constructor test
    @Test
    //valid inputs set every field correctly and status ACTIVE
    public void ConstructorValidInputsTest() {
        //Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        //ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        //Booking booking = new Booking(7, vehicle, slot, START, END, 20.0);

        assertEquals(7, booking.getBookingId());
        assertSame(vehicle, booking.getVehicle());
        assertSame(slot, booking.getParkingSlot());
        assertEquals(START, booking.getStartTime());
        assertEquals(END, booking.getEndTime());
        assertEquals(20.0, booking.getAmount());
        assertEquals(BookingStatus.ACTIVE, booking.getBookingStatus());
    }

    @Test
    // two bookings keep their own values (no shared state)
    public void twoBookings_areIndependent() {

        LocalDateTime start2 = START.plusDays(1);
        LocalDateTime end2 = END.plusDays(1);
        Booking other = new Booking(8, vehicle, slot, start2, end2, 35.0);

        assertEquals(7, booking.getBookingId());
        assertEquals(8, other.getBookingId());
        assertEquals(START, booking.getStartTime());
        assertEquals(start2, other.getStartTime());
        assertEquals(20.0, booking.getAmount(), 0.0);
        assertEquals(35.0, other.getAmount(), 0.0);

    }




    @Test
    //zero amount is accepted (no validation in Booking itself
    public void ConstructorZeroAmountTest() {

        Booking zeroAmountBooking = new Booking(10, vehicle, slot, START, END, 0.0);
        assertEquals(0.0, zeroAmountBooking.getAmount());
        assertEquals(BookingStatus.ACTIVE, zeroAmountBooking.getBookingStatus());

    }


    @Test
    //negative amount is accepted
    public void ConstructorNegativeAmountTest() {

        Booking negativeAmountBooking = new Booking(11, vehicle, slot, START, END, -50.0);
        assertEquals(-50.0, negativeAmountBooking.getAmount());

    }
     @Test
    //startTime equal to endTime is accepted

   public void ConstructorStartEqualsEndTest() {

        Booking sameInstantBooking = new Booking(12, vehicle, slot, START, START, 10.0);
        assertEquals(START, sameInstantBooking.getStartTime());
        assertEquals(START, sameInstantBooking.getEndTime());
        assertEquals(BookingStatus.ACTIVE, sameInstantBooking.getBookingStatus());


     }


    @Test
    //startTime after endTime is accepted
    public void ConstructorStartAfterEndTest() {

        LocalDateTime earlier = START.minusHours(1);
        Booking reversedBooking = new Booking(13, vehicle, slot, START, earlier, 10.0);
        assertEquals(START, reversedBooking.getStartTime());
        assertEquals(earlier, reversedBooking.getEndTime());
        assertEquals(BookingStatus.ACTIVE, reversedBooking.getBookingStatus());

    }


    // completeBooking()

    @Test
    //completeBooking: ACTIVE -> COMPLETED
    public void completeBookingTest() {

        //Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        //ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        //Booking booking = new Booking(7, vehicle, slot, START, END, 20.0);
        booking.completeBooking();
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());

    }


    @Test
   //completeBooking: does not alter id/times/amount
    public void CompleteBookingDoesNotAlterOtherFieldsTest() {

        booking.completeBooking();
        assertEquals(7, booking.getBookingId());
        assertSame(vehicle, booking.getVehicle());
        assertSame(slot, booking.getParkingSlot());
        assertEquals(START, booking.getStartTime());
        assertEquals(END, booking.getEndTime());
        assertEquals(20.0, booking.getAmount());

    }


    @Test
    //completeBooking: calling twice keeps status COMPLETED (idempotent)
    public void CompleteBookingCalledTwiceTest() {

        booking.completeBooking();
        booking.completeBooking();
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());

    }


    @Test
    //completeBooking: after cancelBooking() still overwrites to COMPLETED
    public void CompleteBookingAfterCancelledTest() {

        booking.cancelBooking();
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());


        booking.completeBooking();
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());

    }


    // cancelBooking()


    // @Test
    // public void cancelBookingTest() {
    //Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
    //ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
    //Booking booking = new Booking(7, vehicle, slot, START, END, 20.0);
    //booking.cancelBooking();
    // assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    //}
    @Test
    //cancelBooking: ACTIVE -> CANCELLED
    public void CancelBookingFromActiveTest() {

        //Vehicle vehicle = new Vehicle(1, VehicleType.CAR, 100.0);
        //ParkingSlot slot = new ParkingSlot("S1", ParkingSlotType.REGULAR);
        //Booking booking = new Booking(7, vehicle, slot, START, END, 20.0);
        booking.cancelBooking();
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());

    }


    @Test
    //cancelBooking: does not alter id/times/amount
    public void CancelBookingDoesNotAlterOtherFieldsTest() {

        booking.cancelBooking();
        assertEquals(7, booking.getBookingId());
        assertSame(vehicle, booking.getVehicle());
        assertSame(slot, booking.getParkingSlot());
        assertEquals(START, booking.getStartTime());
        assertEquals(END, booking.getEndTime());
        assertEquals(20.0, booking.getAmount());

    }


    @Test
   //cancelBooking: calling twice keeps status CANCELLED (idempotent)
    public void CancelBookingCalledTwiceTest() {

        booking.cancelBooking();
        booking.cancelBooking();
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());

    }


    @Test
    //cancelBooking: after completeBooking() still overwrites to CANCELLED
    public void CancelBookingAfterCompletedTest() {

        booking.completeBooking();
        assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());


        booking.cancelBooking();
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());

    }


    // Booking.toString
    @Test
//toString: ACTIVE booking shows id, amount and status

    public void ToStringActiveTest() {

        String text = booking.toString();
        assertTrue(text.contains("bookingId=7"));
        assertTrue(text.contains("amount=20.0"));
        assertTrue(text.contains("bookingStatus=ACTIVE"));

    }


    @Test
//toString: shows COMPLETED after completeBooking()
    public void ToStringAfterCompleteTest() {

        booking.completeBooking();
        assertTrue(booking.toString().contains("bookingStatus=COMPLETED"));

    }


    @Test
   //toString: shows CANCELLED after cancelBooking()
    public void ToStringAfterCancelTest() {

        booking.cancelBooking();
        assertTrue(booking.toString().contains("bookingStatus=CANCELLED"));

    }


    //spec expectation


    //D-I1--->Defect 1
    @Test
    //  constructor should reject startTime == endTime (zero-length window)
    public void ConstructorStartEqualsEnd_shouldThrowTest() {

        assertThrows(RuntimeException.class,
                () -> new Booking(12, vehicle, slot, START, START, 10.0)
        );

    }


    //D-I2--->Defect 2
    @Test
    //  constructor should reject startTime after endTime (reversed window)
    public void ConstructorStartAfterEnd_shouldThrowTest() {

        LocalDateTime earlier = START.minusHours(1);
             assertThrows(RuntimeException.class,
                () -> new Booking(13, vehicle, slot, START, earlier, 10.0)
      );

    }


    //D-I3--->Defect 3
    @Test
    // a CANCELLED booking must not become COMPLETED
    public void CompleteBookingAfterCancelled_shouldNotChangeStatusTest() {

        booking.cancelBooking();
        try {
            booking.completeBooking();

        } catch (RuntimeException ignored) {

            // rejecting the transition is also acceptable

        }
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
    }


    //D-I4--->Defect 4
    @Test
    //  a COMPLETED booking must not become CANCELLED
    public void CancelBookingAfterCompleted_shouldNotChangeStatusTest() {

        booking.completeBooking();
        try {
            booking.cancelBooking();

        } catch (RuntimeException ignored) {

            // rejecting the transition is also acceptable
        }
       assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());

    }



    // ParkingSystem Integration Tests

    @Test
// Valid booking charges vehicle and credits system wallet correctly
    public void testParkingSystemBookSuccess() {


        ParkingSystem system = ParkingSystem.getInstance();
        system.resetForTesting();
        system.addVehicle(vehicle);
        system.addParkingSlot(slot);


        // 2 Hours booking
        Booking sysBooking = system.book(vehicle, slot, START, END);


        // Price = 2 hours * 10 (base) * 1.0 (CAR) * 1.0 (REGULAR) = 20.0
        assertEquals(20.0, sysBooking.getAmount(), 0.001);
        assertEquals(80.0, vehicle.getBalance(), 0.001); // 100 - 20
        assertEquals(20.0, system.getBalance(), 0.001); // System wallet 20
    }


    @Test
//ParkingSystem.completeBooking: Transfers 80% to slot and keeps 20% in system")
    public void testParkingSystemCompleteBooking() {

        ParkingSystem system = ParkingSystem.getInstance();
        system.resetForTesting();

        system.addVehicle(vehicle);
        system.addParkingSlot(slot);

        Booking sysBooking = system.book(vehicle, slot, START, END);
        system.completeBooking(sysBooking);

        assertEquals(BookingStatus.COMPLETED, sysBooking.getBookingStatus());
        assertEquals(16.0, slot.getBalance(), 0.001);//80% of 20.0
        assertEquals(4.0, system.getBalance(), 0.001); // 20% of 20.0
    }


    @Test
    //ParkingSystem.cancelBooking: Refunds 90% to vehicle and keeps 10% in system
    public void testParkingSystemCancelBooking() {

        ParkingSystem system = ParkingSystem.getInstance();
        system.resetForTesting();

        system.addVehicle(vehicle);
        system.addParkingSlot(slot);

        Booking sysBooking = system.book(vehicle, slot, START, END);
        system.cancelBooking(sysBooking);
        assertEquals(BookingStatus.CANCELLED, sysBooking.getBookingStatus());
        assertEquals(98.0, vehicle.getBalance(), 0.001); // 80 + 18 (90% refund)
        assertEquals(2.0, system.getBalance(), 0.001); // 10% retained

    }


    //D-I5--->Defect 5
    @Test
    //Partial hour booking truncates duration to 0 hours resulting in 0.0 charge
    public void testParkingSystemBookPartialHour() {

        ParkingSystem system = ParkingSystem.getInstance();
        system.resetForTesting();

        system.addVehicle(vehicle);
        system.addParkingSlot(slot);

        // 30 minute booking
        LocalDateTime start = START;
        LocalDateTime end = START.plusMinutes(30);

        Booking sysBooking = system.book(vehicle, slot, start, end);

        // verify actual computed value
        System.out.println("Actual Computed Amount for 30 mins: " + sysBooking.getAmount());
        assertEquals(0.0, sysBooking.getAmount(), 0.001);

    }


    //D-I6--->Defect 6
    @Test
    //Double completeBooking() allows double fund transfer from system to slot wallet
    public void testParkingSystemDuplicateComplete() {

        ParkingSystem system = ParkingSystem.getInstance();
        system.resetForTesting();

        system.addVehicle(vehicle);
        system.addParkingSlot(slot);

         Booking sysBooking = system.book(vehicle, slot, START, END); // Amount: 20.0


        // First completion: Slot gets 16.0 (80%), System retains 4.0 (20%)
         system.completeBooking(sysBooking);


        // Second completion
        system.completeBooking(sysBooking);


        assertEquals(BookingStatus.COMPLETED, sysBooking.getBookingStatus());
        assertEquals(32.0, slot.getBalance(), 0.001); //  Slot received 80% twice (16.0 + 16.0)![cite: 2, 5]
    }


    //D-I7--->Defect 7
    @Test
    //Double cancelBooking() allows duplicate refund to vehicle wallet
    public void testParkingSystemDuplicateCancel() {


        ParkingSystem system = ParkingSystem.getInstance();
        system.resetForTesting();


        system.addVehicle(vehicle);
        system.addParkingSlot(slot);


        Booking sysBooking = system.book(vehicle, slot, START, END); // Amount: 20.0, Vehicle balance left: 80.0[

        // First cancellation: 90% refund (18.0) -> Vehicle balance becomes 98.0
        system.cancelBooking(sysBooking);

        // Second cancellation
        system.cancelBooking(sysBooking);

        assertEquals(BookingStatus.CANCELLED, sysBooking.getBookingStatus());
        assertEquals(116.0, vehicle.getBalance(), 0.001);

    }




}

