package com.fungisearch.fudriver.common.handler;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.Exception.CycleStartDayAfterFirstHarvestDayException;
import com.fungisearch.fudriver.exception.*;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.PersistenceException;

/**
 * Created by marcin on 02.03.16.
 */
@ControllerAdvice
public class ExceptionController {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler({StaleObjectStateException.class })
    ResponseEntity<Object> handleStaleObjectState(RuntimeException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.error(ex.getMessage());
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"concurrentWrite"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotClosedTimeSheetException.class })
    ResponseEntity<Object> handleNotClosedTimeSheetException(RuntimeException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.error(ex.getMessage());
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"timeSheetNotClosed"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ClosedTimeSheetException.class })
    ResponseEntity<Object> handleClosedTimeSheetException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"timeSheetClosed"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BarcodeAlreadyUsedException.class })
    ResponseEntity<Object> handleBarcodeAlreadyUsedException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"barcodeAlreadyUsed"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BarcodeNotReservedException.class })
    ResponseEntity<Object> handleBarcodeNotReservedException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"barcodeNotReserved"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RfidDuplicationException.class })
    ResponseEntity<Object> handleRfidDuplicationException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"RfidDuplication"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RfidNotExistsException.class })
    ResponseEntity<Object> handleRfidNotExistsExeption(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"RfidNotExists"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WarehouseBrcNotReservedException.class })
    ResponseEntity<Object> handleWarehouseNotReservedException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"Box not reserved"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WarehouseBrcAlreadyReservedException.class })
    ResponseEntity<Object> handleWarehouseAlreadyReservedException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"Box already reserved"),headers, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({BatchFullyReservedException.class })
    ResponseEntity<Object> handleBatchFullyReservedException(RuntimeException ex, WebRequest request){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"Batch fully reserved"),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({StartDateAfterEndDateException.class })
    ResponseEntity<Object> handleStartDayAfterEndDayException(RuntimeException ex, WebRequest request){
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"Start day after end day"),headers, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({CycleStartDayAfterFirstHarvestDayException.class })
    ResponseEntity<Object> handleStartDayAfterFirstHarvestDayException(RuntimeException ex, WebRequest request){
        CycleStartDayAfterFirstHarvestDayException exception = (CycleStartDayAfterFirstHarvestDayException) ex;
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(new CommandResult(new Long(exception.getCycleId()),exception.getFirstDate(), CommandResult.Status.ERROR,exception.getMessage()),headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AssignedOpenMonthsException.class })
    ResponseEntity<Object> handleAssignedOpenMonthsException(RuntimeException ex, WebRequest request){
        AssignedOpenMonthsException exception = (AssignedOpenMonthsException) ex;
        logger.error(ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(new CommandResult(CommandResult.Status.ERROR,exception.getMessage()),headers, HttpStatus.BAD_REQUEST);
    }


/*
    @ExceptionHandler({PersistenceException.class })
    ResponseEntity<Object> handlePersistenceException(RuntimeException ex, WebRequest request){
        logger.error("--------------------------------------- PERSISTENCE EXCEPTION");
        logger.error(ex.getMessage());
        logger.error(ex.getStackTrace().toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity( new CommandResult(CommandResult.Status.ERROR,"PersistenceException"),headers, HttpStatus.BAD_REQUEST);
    }
*/

}
