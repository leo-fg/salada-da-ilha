package br.com.saladadailha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.saladadailha.databinding.FragmentCheckoutBinding
import br.com.saladadailha.model.DeliveryType
import br.com.saladadailha.model.PaymentType
import br.com.saladadailha.viewmodel.CartViewModel
import br.com.saladadailha.viewmodel.CheckoutViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CheckoutFragment : Fragment() {
    
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    
    private val cartViewModel: CartViewModel by viewModels()
    private val checkoutViewModel: CheckoutViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupDeliveryRadioGroup()
        setupPaymentRadioGroup()
        observeData()
        setupConfirmButton()
    }
    
    private fun setupDeliveryRadioGroup() {
        binding.radioGroupDelivery.setOnCheckedChangeListener { _, checkedId ->
            val deliveryType = when (checkedId) {
                binding.radioPickup.id -> DeliveryType.PICKUP
                binding.radioDelivery.id -> DeliveryType.DELIVERY
                else -> DeliveryType.PICKUP
            }
            
            binding.inputLayoutAddress.visibility = if (deliveryType == DeliveryType.DELIVERY) {
                View.VISIBLE
            } else {
                View.GONE
            }
            
            val address = if (deliveryType == DeliveryType.DELIVERY) {
                binding.editAddress.text.toString()
            } else {
                ""
            }
            
            checkoutViewModel.setDeliveryMethod(deliveryType, address)
        }
        
        
        binding.radioPickup.isChecked = true
        checkoutViewModel.setDeliveryMethod(DeliveryType.PICKUP)
    }
    
    private fun setupPaymentRadioGroup() {
        binding.radioGroupPayment.setOnCheckedChangeListener { _, checkedId ->
            val paymentType = when (checkedId) {
                binding.radioCreditCard.id -> PaymentType.CREDIT_CARD
                binding.radioDebitCard.id -> PaymentType.DEBIT_CARD
                binding.radioPix.id -> PaymentType.PIX
                binding.radioCash.id -> PaymentType.CASH
                else -> PaymentType.CREDIT_CARD
            }
            
            binding.layoutCardDetails.visibility = if (paymentType == PaymentType.CREDIT_CARD || 
                                                     paymentType == PaymentType.DEBIT_CARD) {
                View.VISIBLE
            } else {
                View.GONE
            }
            
            val cardDetails = if (paymentType == PaymentType.CREDIT_CARD || 
                                paymentType == PaymentType.DEBIT_CARD) {
                mapOf(
                    "cardNumber" to binding.editCardNumber.text.toString(),
                    "cardholderName" to binding.editCardholderName.text.toString(),
                    "expiryDate" to binding.editExpiryDate.text.toString(),
                    "cvv" to binding.editCvv.text.toString()
                )
            } else {
                emptyMap()
            }
            
            checkoutViewModel.setPaymentMethod(paymentType, cardDetails)
        }
        
        
        binding.radioCreditCard.isChecked = true
        checkoutViewModel.setPaymentMethod(PaymentType.CREDIT_CARD)
    }
    
    private fun observeData() {
        lifecycleScope.launch {
            cartViewModel.subtotal.collect { subtotal ->
                binding.txtSubtotal.text = formatPrice(subtotal)
                updateTotal(subtotal)
            }
        }
        
        lifecycleScope.launch {
            checkoutViewModel.deliveryFee.collect { fee ->
                binding.txtDeliveryFee.text = formatPrice(fee)
                updateTotal(cartViewModel.subtotal.value)
            }
        }
        
        lifecycleScope.launch {
            checkoutViewModel.orderCreated.collect { orderId ->
                if (orderId != null) {
                    findNavController().navigate(
                        CheckoutFragmentDirections.actionCheckoutFragmentToOrderConfirmationFragment(
                            orderId.toString()
                        )
                    )
                    checkoutViewModel.resetOrder()
                }
            }
        }
    }
    
    private fun updateTotal(subtotal: Double) {
        val deliveryFee = checkoutViewModel.deliveryFee.value
        val total = subtotal + deliveryFee
        binding.txtTotal.text = formatPrice(total)
    }
    
    private fun setupConfirmButton() {
        binding.btnConfirmOrder.setOnClickListener {
            
            if (binding.radioDelivery.isChecked && binding.editAddress.text.toString().isBlank()) {
                binding.editAddress.error = "Informe o endereço de entrega"
                return@setOnClickListener
            }
            
            
            if ((binding.radioCreditCard.isChecked || binding.radioDebitCard.isChecked)) {
                if (binding.editCardNumber.text.toString().isBlank()) {
                    binding.editCardNumber.error = "Informe o número do cartão"
                    return@setOnClickListener
                }
                if (binding.editCardholderName.text.toString().isBlank()) {
                    binding.editCardholderName.error = "Informe o nome no cartão"
                    return@setOnClickListener
                }
                if (binding.editExpiryDate.text.toString().isBlank()) {
                    binding.editExpiryDate.error = "Informe a data de validade"
                    return@setOnClickListener
                }
                if (binding.editCvv.text.toString().isBlank()) {
                    binding.editCvv.error = "Informe o código de segurança"
                    return@setOnClickListener
                }
                
                
                val cardDetails = mapOf(
                    "cardNumber" to binding.editCardNumber.text.toString(),
                    "cardholderName" to binding.editCardholderName.text.toString(),
                    "expiryDate" to binding.editExpiryDate.text.toString(),
                    "cvv" to binding.editCvv.text.toString()
                )
                
                val paymentType = if (binding.radioCreditCard.isChecked) {
                    PaymentType.CREDIT_CARD
                } else {
                    PaymentType.DEBIT_CARD
                }
                
                checkoutViewModel.setPaymentMethod(paymentType, cardDetails)
            }
            
            
            if (binding.radioDelivery.isChecked) {
                checkoutViewModel.setDeliveryMethod(
                    DeliveryType.DELIVERY,
                    binding.editAddress.text.toString()
                )
            }
            
            
            checkoutViewModel.createOrder(cartViewModel.subtotal.value)
        }
    }
    
    private fun formatPrice(price: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(price)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
