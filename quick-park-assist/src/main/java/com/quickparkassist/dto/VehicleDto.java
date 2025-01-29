package com.quickparkassist.dto;



    public class VehicleDto {

        private String vehicleNumber;
        private String vehicleModel;
        private String hasElectric;

        public String getVehicleNumber() {
            return vehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            this.vehicleNumber = vehicleNumber;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }

        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public String getHasElectric() {
            return hasElectric;
        }

        public void setHasElectric(String hasElectric) {
            this.hasElectric = hasElectric;
        }

        // default constructor
        public VehicleDto() {}


        public VehicleDto(String vehicleNumber, String vehicleModel, String hasElectric) {
            this.vehicleNumber = vehicleNumber;
            this.vehicleModel = vehicleModel;
            this.hasElectric = hasElectric;
        }


        // Getters and Setters
    }


